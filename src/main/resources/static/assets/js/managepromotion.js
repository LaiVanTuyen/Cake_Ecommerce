$(document).ready(function () {
   $('.header-item').each(function(index, element){
       $(element).click(function(){         
           switchOption(index, $(element));
       })
   })
   // add promotion
   $('#add-promotion-btn').click(function(e){
       addPromotion();
       toggleFeedback($(".add-promotion-success"));
   })
   $('#cancel-promotion-btn').click(function(e){
       refreshAddPromotion();
   })

    // edit promotion
    $("#edit-select-promotion").change(function (){
        var promotionId = $(this).find("option:selected").attr("promotion-id");
        loadEditPromotionData(promotionId);
    })
    $("#edit-promotion-btn").click(function (){
        checkValidEditPromotion();
    })

    // product promotion
    $("#product-select-promotion").change(function (){
        var promotionId = $(this).find("option:selected").attr("promotion-id");
        loadProductInPromotionTable(promotionId);
    })

    $("#product-add-promotion-btn").click(function (){
        productAddPromotion();
    })
    $("#product-remove-promotion-btn").click(function (){
        productRemovePromotion();
    })
    loadProductNotInPromotionTable();

   //search product
    $("#search-pnip-button").click(function (){
       searchProductNotInPromotion();
    });
    $("#search-pip-button").click(function (){
        searchProductInPromotion();
    })
});

function switchOption(index, element){
    $('.header-item').each(function(){
        $(this).removeClass('object--border-bottom');
    })
    element.addClass('object--border-bottom')

    $('.body-item').addClass('object--disapper');
    $('.body-item').eq(index).removeClass('object--disapper');
}

function toggleFeedback(element){
    element.removeClass("object--disapper");
    setTimeout(function (){
        element.addClass("object--disapper");
    },3000)
}

// Add promotion

function addPromotion(){
    if(checkValidAddPromotion() == true){
        var url = PATH_API + '/manage/promotion';
        var form = document.forms.namedItem("add-promotion-form");
        var formData = new FormData(form);
        $.ajax({
            url: url,
            data: formData,
            type: "POST",
            enctype: 'multipart/form-data',
            contentType: false,
            processData: false,
        }).done(function (){
            refreshAddPromotion();
            toggleFeedback($('.add-promotion-success'));
        })
    }
}

function checkValidAddPromotion(){
    var title = $('#add-title-promotion').val();
    var desc = $('#add-desc-promotion').val();
    var file = $('#add-file-promotion').val();
    var discount = $('#add-discount-promotion').val();
    if(title == '' || desc == '' || file == '' || discount == ''){
        $('.add-promotion-feedback').removeClass('object--disapper');
        return false;
    }
    else{
        $('.add-promotion-feedback').addClass('object--disapper');
        return true;
    }
}

function refreshAddPromotion(){
    $('#add-title-promotion').val('');
    $('#add-desc-promotion').val('');
    $('#add-file-promotion').val('');
    $('#add-discount-promotion').val('');
    $('.add-promotion-feedback').addClass('object--disapper');
}

// edit promotion
function loadEditPromotionData(promotionId){
   if(promotionId != undefined){
       var url = PATH_API + `/promotion/${promotionId}`
       $.get(url, function (response){
            $("#edit-title-promotion").val(response.title);
            $("#edit-desc-promotion").val(response.description);
            $("#edit-discount-promotion").val(response.discount);
       })
   }
}

function checkValidEditPromotion(){
    var title = $("#edit-title-promotion").val();
    var desc = $("#edit-desc-promotion").val();
    var discount = $("#edit-discount-promotion").val();
    if(title == '' || desc == '' || discount == ''){
        $(".edit-promotion-feedback").removeClass("object--disapper");
    }
    else{
        $(".edit-promotion-feedback").addClass("object--disapper");
        updatePromotion();
    }
}

function updatePromotion(){
    var promotionId = $("#edit-select-promotion").find("option:selected").attr("promotion-id");
    var form = document.forms.namedItem("edit-promotion-form");
    var formData = new FormData(form);
    var url = PATH_API + `/manage/promotion/${promotionId}`
    $.ajax({
        url: url,
        data: formData,
        type: "PUT",
        enctype: 'multipart/form-data',
        contentType: false,
        processData: false
    }).done(function (){
        toggleFeedback($('.edit-promotion-success'));
    })
}

// product promotion
function loadProductInPromotionTable(promotionId){
    // load products in promotion
    if(promotionId != undefined){
        var url = PATH_API + `/promotion/${promotionId}/products`
        $.get(url, function (listProduct){
            showPinPTable(listProduct);
        })
    }
}
function loadProductNotInPromotionTable(){
    var url = PATH_API + '/products-notdiscount';
    $.get(url, function (listProduct){
        showPnotInP(listProduct);
    })
}

    // add promotion to product
function productAddPromotion() {
    console.log("Thêm promotion")
    var checkbox = $("#product-not-in-promotion").find('input')
    var productIdList = new Array();
    checkbox.each(function (){
        if($(this).is(":checked")){
            productIdList.push($(this).attr('product-id'));
        }
    })
    if(productIdList.length > 0){
        var promotionId = $("#product-select-promotion").find("option:selected").attr("promotion-id");
        productIdList.push(promotionId)
        var url = PATH_API + "/products-addpromotion/"
        var dataJson = {
            listProductId: productIdList
        }
        $.ajax({
            url: url,
            data: dataJson,
            type: "POST",
            traditional: true,
        }).done(function (){
            loadProductInPromotionTable($('#product-select-promotion').find("option:selected").attr("promotion-id"));
            loadProductNotInPromotionTable();
            toggleFeedback($('#add-productpromotion-success'));
        })
    }
}
    //remove promotion in product
function productRemovePromotion(){
    console.log("Xóa promotion")
    var checkbox = $("#product-in-promotion").find('input')
    var listId = new Array();
    checkbox.each(function (){
        if($(this).is(":checked")){
            listId.push($(this).attr("product-id"));
        }
    })
    if(listId.length > 0){
        var url = PATH_API + "/products-removepromotion";
        var dataJson = {listProductId: listId};
        $.ajax({
            url: url,
            data: dataJson,
            type: "POST",
            traditional: true,
        }).done(function (){
            loadProductInPromotionTable($('#product-select-promotion').find("option:selected").attr("promotion-id"));
            loadProductNotInPromotionTable();
            toggleFeedback($('#remove-productpromotion-success'));
        })
    }
}

    //search product not in promotion
function searchProductNotInPromotion(){
    var input = $("#search-pnip-input").val();
    var url = PATH_API + "/products-nip"
    $.ajax({
        url: url,
        data: {
            name: input
        },
        type: "GET"
    }).done(function (response){
        showPnotInP(response);
    })
}
    //search product in promotion
function searchProductInPromotion(){
    var input = $("#search-pip-input").val();
    var url = PATH_API + "/products-ip";
    $.ajax({
        url, url,
        data: {
            name: input,
            promotionId: $("#product-select-promotion").find("option:selected").attr("promotion-id")
        },
        type: "GET"
    }).done(function (response){
        showPinPTable(response);
    })
}

    // PinP: product in promotion
function showPinPTable(listProduct){
    var productTable1 = $("#product-in-promotion");
    productTable1.children('tbody').empty();
    var stt = 1;
    listProduct.forEach(product =>{
        productTable1.append(($('<tr>').attr('product-id', product.productId))
            .append($('<td>').append(stt++))
            .append($('<td>')
                .append($('<a>', {href: `/product/${product.productId}`}).addClass('text-decoration-none d-flex text-black align-items-center')
                    .append(($('<img>',{src:`${product.imageName}`})).addClass('w-25'))
                    .append(($('<span>',{id: 12, text: product.name})).addClass('fw-bold w-75'))
                )
            )
            .append($('<td>').append(product.price))
            .append($('<td>').append(product.discountPrice))
            .append($('<td>')
                .append(($('<input>', {type: "checkbox"})).attr("product-id", product.productId))
            )
        )
    })
}

    // PnotInP: product not in promotion
function showPnotInP(listProduct){
    var productTable1 = $("#product-not-in-promotion");
    productTable1.children('tbody').empty();
    var stt = 1;
    listProduct.forEach(product =>{
        productTable1.append(($('<tr>').attr('product-id', product.productId))
            .append($('<td>').append(stt++))
            .append($('<td>')
                .append($('<a>', {href: `/product/${product.productId}`}).addClass('text-decoration-none d-flex text-black align-items-center')
                    .append(($('<img>',{src:`${product.imageName}`})).addClass('w-25'))
                    .append(($('<span>',{id: 12, text: product.name})).addClass('fw-bold w-75'))
                )
            )
            .append($('<td>').append(product.price))
            .append($('<td>')
                .append(($('<input>', {type: "checkbox"})).attr("product-id", product.productId))
            )
        )
    })
}