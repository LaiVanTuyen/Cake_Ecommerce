let productId = $('#product-item').attr('product-id');
$(document).ready(function() {

    $("#owl-demo").owlCarousel({
        items : 4, //10 items above 1000px browser width
        itemsDesktop : [1000,4], //5 items between 1000px and 901px
        itemsDesktopSmall : [900,2], // betweem 900px and 601px
        itemsTablet: [600,2], //2 items between 600 and 0;
        itemsMobile: [100, 2]
    });
    selectColor();
    checkQuantity();

    $('#buy-product-frm').submit(function (e){
        e.preventDefault();
    })
    $('#buyNowBtn').click(checkOutProduct);
    $('#addToCartBtn').click(addToCart);
});

var elements = document.querySelectorAll('.color-label');
elements.forEach(element => {
    var heigh = element.scrollHeight;
    var top = String(-heigh);
    element.style.top = top + 'px';
})

// select color
function selectColor(){
    var colorSelected = document.querySelector('.item-color-selected');
    var colorItems = document.querySelectorAll('.color-item');
    colorItems.forEach(colorItem => {

        colorItem.addEventListener("click", function(){
            var colorID = colorItem.getAttribute('colorID');
            var color = colorItem.getAttribute('color');
            colorSelected.innerText = color;
            colorSelected.setAttribute('value', colorID);

            colorItems.forEach(value => {
                value.classList.remove('border--dark');
            });

            colorItem.classList.add('border--dark');
            resetQtyNFeedback();
            refreshSize();
            checkProductBtn();
        });
    })
}

function resetQtyNFeedback(){
    document.querySelector('.item-qty-selected').value = 1;
    document.querySelector('.feedback-validnumber').innerText = '';
}

// select size
function selectSize(element){
    var sizeSelected = document.querySelector('.item-size-selected');
    element.click(function (){

        resetQtyNFeedback();

        sizeSelected.setAttribute('value', $(this).attr('sizeID'));
        sizeSelected.innerText = $(this).attr('size');
        $('#buy-product-frm').attr('quantity-id', $(this).attr('quantity-id'));
        $('#buy-product-frm').attr('quantity', $(this).attr('quantity'));
        $('.size-item').each(function (){
            $(this).removeClass('border--dark')
        })

        $(this).addClass('border--dark')

        checkProductBtn();
    });
}
// refresh select color
function refreshSize(){
    $('.size-item').each(function (){
        $(this).removeClass('border--dark')
    })
    $('.item-size-selected').attr('value', '');
    $('.item-size-selected').text('');
    $('#buy-product-frm').attr('quantity-id', '')
    $('#buy-product-frm').attr('quantity', '')
}
// add quantity
function increaseQuantity(){
    var qtySelected = document.querySelector('.item-qty-selected');
    if(isNaN(qtySelected.value) == false){
        qtySelected.value = Number(qtySelected.value) + 1;
    }
    checkIsNumber(qtySelected.value);
}

function decreaseQuantity(){
    var qtySelected = document.querySelector('.item-qty-selected');
    if(qtySelected.value > 1){
        qtySelected.value = Number(qtySelected.value) - 1;
    }
    checkIsNumber(qtySelected.value);
}

// check product button
function checkProductBtn(){
    var colorSelected = document.querySelector('.item-color-selected');
    var sizeSelected = document.querySelector('.item-size-selected');
    var productBtns = document.querySelectorAll('.productBtn');
    if($('.select-item-color').attr('value') == '' || sizeSelected.getAttribute('value') == ''){
        productBtns.forEach(productBtn => {
            productBtn.disabled = true;
        })
    }
    if($('#buy-product-frm').attr('quantity') == '' || $('#buy-product-frm').attr('quantityId') == ''){
        productBtns.forEach(productBtn => {
            productBtn.disabled = true;
        })
    }
    if($('.select-item-color').length == 0 && sizeSelected.getAttribute('value') != ''){
        productBtns.forEach(productBtn => {
            productBtn.disabled = false;
        })
    }
    else if(colorSelected.getAttribute('value') != '' && sizeSelected.getAttribute('value') != ''){
        productBtns.forEach(productBtn => {
            productBtn.disabled = false;
        })
    }
}

// // check is number
function checkIsNumber(value){
    var feedback = document.querySelector('.feedback-validnumber');
    feedback.innerText = '';

    var maxQty = 20;
    if($('#buy-product-frm').attr('quantity') != ''){
        maxQty = Math.min(20, $('#buy-product-frm').attr('quantity'));
    }

    if(isNaN(value) || value < 1){
        feedback.innerText = 'Số lượng không hợp lệ';

        document.querySelectorAll('.productBtn').forEach(btn => {
            btn.disabled = true;
        })
    }
    else if(value > maxQty){
        var qty = $('#buy-product-frm').attr('quantity');
        feedback.innerText = `Số lượng không đủ, số lượng tối đa là ${maxQty}`;

        document.querySelectorAll('.productBtn').forEach(btn => {
            btn.disabled = true;
        })
    }
    else if(checkProductBtn()){
        document.querySelectorAll('.productBtn').forEach(btn => {
            btn.disabled = false;
        })
    }
}


// check quantity
function checkQuantity(){
    var url = PATH_API + `/product/${productId}/quantity`
    $.get(url, function (response){
       if(response.noColor == true){
            $('.size-item').each(function (){
                ennabledSizeBtn($(this));
                response.quantityBySize.forEach(resItem => {
                    if(resItem.size == $(this).attr('size')){
                        $(this).attr("quantity-id", resItem.quantityId);
                        $(this).attr("quantity", resItem.quantity);
                    }
                    if(resItem.size == $(this).attr('size') && resItem.quantity == 0){
                        disabledSizeBtn($(this))
                    }
                })
            })
       }
       else{
           $('.color-item').each(function (){
               $(this).click(function (){
                   refreshSize();
                   checkProductBtn();
                   response.quantityByColor.forEach(resItem => {
                       if($(this).attr('color') == resItem.color){
                           $('.size-item').each(function (){
                               ennabledSizeBtn($(this));
                               resItem.listSize.forEach(size => {
                                   if(size.size == $(this).attr('size')){
                                       $(this).attr("quantity-id", size.quantityId);
                                       $(this).attr("quantity", size.quantity);
                                   }
                                   if(size.size == $(this).attr('size') && size.quantity == 0){
                                       disabledSizeBtn($(this))
                                   }
                               })
                           })
                       }
                   })
               })
           })
       }
    });
}

function disabledSizeBtn(element){
    element.addClass('object--disabled');
    element.off("click");
}
function ennabledSizeBtn(element){
    element.removeClass('object--disabled');
    selectSize(element);
}

// check out
function checkOutProduct(){
    $('#quantityId').val($('#buy-product-frm').attr('quantity-id'));
    $('#quantity').val($('.item-qty-selected').val());
    console.log(  $('#quantityId').val(),  $('#quantity').val())
    $('#buy-product-frm').submit(function (e){
        e.currentTarget.submit();
    });
}

function addToCart(){
    var url = PATH_API + '/cart/';
    var quantityId = $('#buy-product-frm').attr('quantity-id');
    var quantity = $('.item-qty-selected').val();
    var dataJson = {
        quantityId: quantityId,
        quantity: quantity
    }
    $.ajax({
        url: url,
        type: "POST",
        data: JSON.stringify(dataJson),
        contentType: 'application/json'
    }).done(function (response){
        if(response.localeCompare('setcookie') == 0){
            addToCookie(quantityId+'-'+quantity);
        }
        updateCart();
    })
}

function addToCookie(name){
    var value = name;
    document.cookie.split(';').forEach(data => {
        console.log(data.split('=')[0].trim());
        if(data.split('=')[0].trim().localeCompare("cartItem") == 0){
            value = value + 'and' + data.split('=')[1].trim();
        }
    })
    document.cookie = 'cartItem=' + value + ';path=/';
}
function updateCart(){
    var currentQty = $('#cart-quantity').text();
    currentQty = Number(currentQty) + 1;
    $('#cart-quantity').text(currentQty);
}