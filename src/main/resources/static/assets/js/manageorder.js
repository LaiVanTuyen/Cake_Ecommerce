$(document).ready(function (){
    viewOrderDetail();
    approvalOrder();
    deniedOrder();
})

function viewOrderDetail(){
    $('.view-order').each(function (){
        $(this).click(function (){
            var orderId = $(this).attr('order-id');
            var orderUrl = PATH_API + `/order/${orderId}`;
            var orderItemUrl = PATH_API + `/order/${orderId}/items`
            $.get(orderUrl, function (response){
                setInforOrderModal(response);
            })
            $.get(orderItemUrl, function (response){
                setInforOrderItemModal(response);
                console.log(response);
            })
        })
    })
}


function approvalOrder(){
    $('.approval-order').each(function (){
        $(this).click(function (){
            var orderId = $(this).attr('order-id');
            console.log(orderId);
            var url = PATH_API + `/order/${orderId}`
            $.ajax({
                url: url,
                data: {
                        action : 1
                    },
                type: "POST",
            }).done(function (){
                loadOrderData();
            })
        })
    })
}

function deniedOrder(){
    $('.denied-order').each(function (){
        $(this).click(function (){
            var orderId = $(this).attr('order-id');
            console.log(orderId);
            var url = PATH_API + `/order/${orderId}`
            $.ajax({
                url: url,
                data: {
                    action : 2
                },
                type: "POST",
            }).done(function (){
                loadOrderData();
            })
        })
    })
}

function loadOrderData(){
    var url = PATH_API + '/order-pending';
    $.get(url, function (response){
        var stt = 1;
        $('#order-table tbody').empty();
        response.forEach(order => {
            $('#order-table tbody').append($('<tr>')
                .append($('<td>').append(stt++).addClass('fw-bold ps-0'))
                .append($('<td>').append(order.orderId))
                .append($('<td>').append(order.buyingDate))
                .append($('<td>').append(
                        `<span th:text="*{order.getPayTotalCurrency()}">${order.payTotalCurrency}</span><i class="fa-solid fa-dong-sign"></i>`
                ))
                .append($('<td>').append(
                    `<span type="button" data-bs-toggle="modal" data-bs-target="#orderDetailModal"
                                                    class="text-decoration-none text-primary view-order"
                                                    order-id = ${order.orderId}>
                                                    Chi tiết
                                                </span>
                                                <span role="button" class="text-decoration-none text-success mx-3 approval-order"
                                                   order-id = ${order.orderId}>
                                                    Duyệt
                                                </span>
                                                <span role="button" class="text-decoration-none text-danger denied-order"
                                                    order-id = ${order.orderId}>
                                                    Hủy
                                                </span>`
                ))
            )
        })
    }).done(function (){
        approvalOrder();
        deniedOrder();
    })

}

function setInforOrderModal(data){
    $('#modal-order-id').text(data.orderId);
    $('#modal-order-buyername').text(data.buyerName);
    $('#modal-order-phonenum').text(data.phoneNumber);
    $('#modal-order-paytotal').text(data.payTotalCurrency);

    var address = data.detail + ', ' + data.ward + ' - ' + data.district + ' - ' + data.province;
    $('#modal-order-address').text(address);
}

function setInforOrderItemModal(listItem){
    listItem.forEach(item => {
        $('#order-item-list').append(
            `<div class="d-flex justify-content-between">
                  <span class="order-item-name">${item.productName}</span>
                  <span>${item.quantity}</span>
                  <span>${item.size.toUpperCase()}  ${item.color.toUpperCase()}</span>
                  <span>${item.totalCurrency}<i class="fa-solid fa-dong-sign"></i></span>
            </div>`
        )
    })
}