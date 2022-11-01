const FORMAT_CURRENCY = amount => {
    return amount.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,")
};

$(document).ready(function(){
    updatePayTotal();
    incQtyItem();
    decQtyItem();

    $('.order-item-total').each(function (){
        formatCurrency($(this));
    })
    removeItem();
})

function removeItem(){

}
function decQtyItem(){
    $('.dec-item-btn').each(function (){
        $(this).click(function (){
            var quantity = $(this).parents(".quantity-item-control").find(".quantity-item-input").val();
            if(quantity > 1){
                quantity = Number(quantity) - 1;
                $(this).parents(".quantity-item-control").find(".quantity-item-input").val(quantity)

                var displayElm = $(this).parents('.checkout-order-item').find('.order-item-total');
                updateItemTotal(displayElm, quantity);
            }
        })
    })
}
function incQtyItem(){
    $('.inc-item-btn').each(function (){
        var remainQty = Math.min(20, Number($(this).attr("remainqty")));
        $(this).click(function (){
            var quantity = Number($(this).parents(".quantity-item-control").find(".quantity-item-input").val());
            if (quantity < remainQty){
                quantity = Number(quantity) + 1;
                $(this).parents(".quantity-item-control").find(".quantity-item-input").val(quantity);

                var displayElm = $(this).parents('.checkout-order-item').find('.order-item-total');
                updateItemTotal(displayElm, quantity);
            }
        })
    })
}

function updateItemTotal(display, quantity){
    var total = Number(quantity * display.attr('item-price'));
    display.text(FORMAT_CURRENCY(total))
    display.attr('total', total);
    updatePayTotal();
}

function updatePayTotal(){
    var total = 0;
    $('.order-item-total').each(function (){
        total += Number($(this).attr('total'));
    })
    $('#order-paytotal-display').text(FORMAT_CURRENCY(total));
}

function removeItem(){
    $('.rm-item-btn').each(function (){
        $(this).click(function (){
            console.log('clicked');
            $(this).parents('.checkout-order-item').remove();
            updatePayTotal();
        })
    })
}

function formatCurrency(element){
    var total = Number(element.text());
    element.text(FORMAT_CURRENCY(total));
}

function showAddressInfor(value){
    var addressId = $('#order-address-select').children('option:selected').attr('address-id');
    var url = PATH_API + `/address/${addressId}`
    $.get(url, function (response){
        $('#order-buyerName-input').val(response.receiverName)
        $('#order-phonenum-input').val(response.phoneNumber)
        $('#order-province-input').val(response.province)
        $('#order-district-input').val(response.district)
        $('#order-ward-input').val(response.ward)
        $('#order-detail-input').val(response.detail)
    })
}