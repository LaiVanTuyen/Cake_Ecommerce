$(document).ready(function (){
    decItemQty();
    incItemQty();
    removeItem();
})
let CURRENCY = amount => {
    return amount.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,")
};
function decItemQty(){
    $('.dec-qty-btn').each(function (){
        $(this).click(function (){
            var quantity = $(this).parents('.item-qty').find('.item-qty-input').val();
            if(quantity > 1){
                quantity = Number(quantity) - 1;
                $(this).parents('.item-qty').find('.item-qty-input').val(quantity);
                var quantityId =  $(this).parents('.item-qty').find('.quantity-id-input').val();

                var cartId = $(this).parents('.order-item').attr('cartId');
                updateQuantity(quantityId, -1, cartId);

                var productTotalElm = $(this).parents('.order-item').find('.item-total');
                updateItemTotal(productTotalElm, quantity);
            }
        })
    })
}
function incItemQty(){
    $('.inc-qty-btn').each(function (){
        var remainQty = Math.min(20, Number($(this).attr("quantity")));
        $(this).click(function (){
            var quantity = Number($(this).parents('.item-qty').find('.item-qty-input').val());
            if (quantity < remainQty){
                quantity = Number(quantity) + 1;
                $(this).parents('.item-qty').find('.item-qty-input').val(quantity);
                var quantityId =  $(this).parents('.item-qty').find('.quantity-id-input').val();

                var cartId = $(this).parents('.order-item').attr('cartId');
                updateQuantity(quantityId, 1, cartId);

                var productTotalElm = $(this).parents('.order-item').find('.item-total');
                updateItemTotal(productTotalElm, quantity);
            }
        })
    })
}

function updateQtyCookie(quantityId, number){
    document.cookie.split(';').forEach(cookieData => {
        if(cookieData.split('=')[0].trim().localeCompare('cartItem') == 0){
            var valueList = cookieData.split('=')[1];
            var value = '';
            valueList.split('and').forEach(data => {
                if(data.search(quantityId) > -1){
                    var qty = data.split('-')[1];
                    qty = Number(qty) + number;
                    var updateQty = data.split('-')[0] + '-' + qty;
                    value +=  updateQty + 'and';
                }
                else {
                    value += data + 'and';
                }
            })
            value = value.substring(0, value.length-3)
            document.cookie = 'cartItem=' + value + ';path=/';

        }
    })
}

function updateQuantity(quantityId, number, cartId){
    if(cartId != -1){
        var url = PATH_API + `/cart/${cartId}`
        $.ajax({
            url: url,
            type: "PUT",
            data: {
                quantity: number,
            }
        })
    }
    else{
        updateQtyCookie(quantityId, number);
    }
}

function updateItemTotal(element,quantity){
    var total = Number($(element).attr('price')) * Number(quantity);
    $(element).val(total);
    $(element).text(CURRENCY(total));
    updatePaytotal();
}

function updatePaytotal(){
    var total = 0;
    $('.item-total').each(function (){
        total += Number($(this).val());
    })
    $('#payTotal').text(CURRENCY(total));
}

function removeItem(){
    $('.rm-item-btn').each(function (){
        $(this).click(function (){
            var quantityId = $(this).parents('.order-item').attr('quantityId');
            var cartId = $(this).parents('.order-item').attr('cartId');
            if(cartId == -1){
                removeItemCookie(quantityId);
                $(this).parents('.order-item').remove();
            }
            else{
                removeItemDB(cartId);
                $(this).parents('.order-item').remove();
            }
        })
    })
}
function removeItemCookie(quantityId){
    document.cookie.split(';').forEach(cookieItem => {
        if(cookieItem.split('=')[0].trim().localeCompare('cartItem') == 0){
            var value = cookieItem.split('=')[1].trim();
            var newValue = "";
            value.split('and').forEach(data => {
                if(data.search(quantityId) == -1){
                    newValue += data + 'and';
                }
            })
            if(newValue.length > 0){
                newValue = newValue.substring(0, newValue.length-3);
            }
            document.cookie = 'cartItem=' + newValue + ';path=/';
        }
    })
    checkCartEmpty()
    updatePaytotal();
}
function removeItemDB(cartID){
    var url = PATH_API + `/cart/${cartID}`
    $.ajax({
        type: "DELETE",
        url: url,
    }).done(function (){
        checkCartEmpty()
        updatePaytotal();
    })
}
function checkCartEmpty(){
    if($('.order-item').length <= 1){
        location.reload();
    }
}