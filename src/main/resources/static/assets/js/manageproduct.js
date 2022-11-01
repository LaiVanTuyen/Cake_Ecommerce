// SC: single color
// MC: mutil color
$(document).ready(function (){
    // add product single color
    $('#single-color-control').change(function (){
        if($('#single-color-control').is(':checked')){
            $('#single-color-body').attr("hidden", false);
            $('#mutil-color-body').attr("hidden", true);
            resetDataAddProduct()
        }
    })
    $('#mutiple-color-control').change(function (){

        if($('#mutiple-color-control').is(':checked')){
            $('#mutil-color-body').attr("hidden", false);
            $('#single-color-body').attr("hidden", true);
            resetDataAddProduct()
        }
    })

    $('#ap-add-btn').click(function (){
       addProduct();
    });

    $("#ap-add-img-btn-sc").click(function (){
        addProductImgToListSC();
    });
    $('#ap-add-qty-btn-sc').click(function (){
        addProductQtyToListSC();
    })

    $('#ap-mc-add-img-btn').click(function (){
        addProductImgToListMC();
    })
    $('#ap-mc-add-qty-btn').click(function (){
        addProductQtyToListMC();
    })

    $('#ap-cancel-btn').click(function (){
        resetDataAddProduct();
    })

    // edit product
    // get list product
    getProductsByCate();

    $('#ep-update-product-infor').click(function (){
        updateProductInfor($(this).attr('product-id'));
    })

    $('#ep-search-btn').click(function (){
        searchProduct();
    })

})
// reset Data Add Product
function resetDataAddProduct(){
    $('.clear').each(function (){
        $(this).val('')
    });
    $('#ap-img-table-sc tbody tr').remove();
    $('#ap-qty-table-sc tbody tr').remove();
    $('#ap-mc-img-table tbody tr').remove();
    $('#ap-mc-qty-table tbody tr').remove();
    $('.ap-warn').attr('hidden', true);
}
// check data empty
function checkEmptyData(tableQty, tableImg){
    $('.clear').each(function (){
        if($(this).val() == ''){
            return false;
        }
    });
    if (tableQty.length == 0 ||  tableImg.length == 0){
        return false;
    }
    return true;
}

function successNotify(element){
    element.attr('hidden', false);
    setTimeout(function (){element.attr('hidden', true)}, 3000)
}

// add product
function addProduct(){
    var tableRowQty;
    var tableRowImg;
    var productSC = $('#single-color-control').is(':checked');

    if(productSC){
        tableRowQty = $('#ap-qty-table-sc tbody tr');
        tableRowImg = $('#ap-img-table-sc tbody tr');
    }
    else {
        tableRowQty = $('#ap-mc-qty-table tbody tr');
        tableRowImg = $('#ap-mc-img-table tbody tr');
    }
    if(checkEmptyData(tableRowQty, tableRowImg)){
        var url = PATH_API + "/product/"
        var dataJson = getProductSCInfor();
        $.ajax({
            url: url,
            type: 'POST',
            data: JSON.stringify(dataJson),
            contentType: 'application/json'
        }).done(function (response){

            var dataImage;
            var dataQty;
            if(productSC){
                dataImage = getProductImageSC(response);
                dataQty = getProductQtySC(response);
            }
            else{
                dataImage = getProductImgMC(response);
                dataQty = getProductQtyMC(response);
            }

            $.ajax({
                url: PATH_API + '/product-image/',
                data: dataImage,
                type: "POST",
                enctype: 'multipart/form-data',
                contentType: false,
                processData: false
            }).done(function (){
            })

            $.ajax({
                url: PATH_API + '/product-quantity/',
                data: JSON.stringify(dataQty),
                type: "POST",
                contentType: 'application/json',
                traditional: true
            }).done(function (){
                successNotify($('.ap-success'));
                resetDataAddProduct();
            }).fail(function (){
                alert('fail');
            })
        })
    }
    else{
        $('.ap-warn').attr('hidden', false);
    }
}

    // add product hs single color
function getProductSCInfor(){
    var name = $('#ap-name-input').val();
    var cate = $('#ap-cate-select').find('option:selected').val();
    var price = $('#ap-price-input').val();
    var dataJson = {
        name: name,
        price:  price,
        code: cate,
    }
    return dataJson;
}

function getProductImageSC(productId){
    var form = new FormData();
    form.append('productId', productId);
    $.each($('input[type=file]'), function (i, file){
       if(file.files[0] != undefined){
           form.append('imageFile[]', file.files[0]);
       }
    });
    return form;
}
function getProductQtySC(productId){
    var dataJson = new Array();
    $('#ap-qty-table-sc tbody tr').each(function (){
        var size = $(this).find('td:eq(0)').html();
        var qty = $(this).find('td:eq(1)').html();
        dataJson.push(
            {
                productId: productId,
                size: size,
                quantity: qty
            }
        )
    })
    return dataJson;
}

function getProductImgMC(productId){
    var form = new FormData();
    form.append('productId', productId);
    $.each($('input[type=file]'), function (i, file){
        if(file.files != undefined){
            form.append('imageFile[]', file.files[0])
        }
    })
    var listColor = new Array();
    $('#ap-mc-img-table tbody tr').each(function (index){
        var color = $(this).find('td:eq(0)').html();
        listColor.push(color);
    })
    form.append("colors", listColor);
    return form;
}

function getProductQtyMC(productId){
    var dataJson = new Array();
    $('#ap-mc-qty-table tbody tr').each(function (){
        var color = $(this).find('td:eq(0)').html();
        var size = $(this).find('td:eq(1)').html();
        var quantity = $(this).find('td:eq(2)').html();
        dataJson.push({
            productId: productId,
            color: color,
            size: size,
            quantity: quantity
        })
    })
    return dataJson;
}

    // add product image to list image(table)
function addProductImgToListSC(){
    var tableRowImg = $('#ap-img-table-sc tbody tr');
    var removeBtn = $('.ap-rm-img-btn-sc')
    removeClickBtn(removeBtn)

    var input = $('#ap-add-img-sc input');
    var listImg = $('#ap-img-table-sc');
    listImg.append($('<tr>')
        .append($('<td>').append(input))
        .append($('<td>').append('<i class="fa-solid fa-minus ms-2 ap-rm-img-btn-sc" role="button"></i>'))
    )
    $('#ap-add-img-sc').prepend('<input type="file" name="imageFile[]" class="form-control input my-2">')

    removeProductImgFromList(tableRowImg, removeBtn);
}

    // add product qty to list qty(table)
function addProductQtyToListSC(){
    var tableRowImg = $('#ap-qty-table-sc tbody tr');
    var removeBtn = $('.ap-rm-qty-btn-sc');

    removeClickBtn(removeBtn);

    var size = $('#ap-size-input-sc').val();
    var qty = $('#ap-qty-input-sc').val();
    var qtyTable = $('#ap-qty-table-sc');
    qtyTable.append($('<tr>')
        .append($('<td>').append(size))
        .append($('<td>').append(qty))
        .append($('<td>').append('<i class="fa-solid fa-minus ap-rm-qty-btn-sc" role="button"></i>'))
    )

    removeProductQtyFromList(tableRowImg, removeBtn);
}

    // add product imgae to list imgae MC
function addProductImgToListMC(){
    var tableImgRows = $('#ap-mc-img-table tbody tr');
    var btnsRemove = $('.ap-mc-rm-img-btn')
    removeClickBtn(btnsRemove);

    var file = $('#ap-mc-add-img input');
    $('#ap-mc-img-table').append($('<tr>')
        .append($('<td>').append($('#ap-mc-cimg-input').val()))
        .append($('<td>').append(file))
        .append($('<td>').append('<i class="fa-solid fa-minus ap-mc-rm-img-btn" role="button">'))
    )
    $('#ap-mc-add-img').prepend(' <input type="file" name="imageFile[]" class="form-control input my-2 me-2">')

    removeProductImgFromList(tableImgRows, btnsRemove);
}
    // add product qty to list qty MC(table)
function addProductQtyToListMC(){
    var tableQtyRows = $('#ap-mc-qty-table tbody tr');
    var btnsRemove = $('.ap-mc-rm-qty-btn');
    removeClickBtn(btnsRemove);

    $('#ap-mc-qty-table').append($('<tr>')
        .append($('<td>').append($('#ap-mc-cqty-input').val()))
        .append($('<td>').append($('#ap-mc-size-input').val()))
        .append($('<td>').append($('#ap-mc-qty-input').val()))
        .append($('<td>').append('<i class="fa-solid fa-minus ap-mc-rm-qty-btn" role="button"></i>'))
    )
    removeProductQtyFromList(tableQtyRows, btnsRemove);
}

// remove product image from list
function removeProductImgFromList(tableRowImg, btnsRemove){
    tableRowImg.each(function (index){
        btnsRemove.eq(index).on('click', function (){
            tableRowImg.eq(index).remove();
        })
    })
}
// remove product qty from list
function removeProductQtyFromList(tableRowQty, btnsRemove){
    tableRowQty.each(function (index){
        btnsRemove.eq(index).on('click', function (){
            tableRowQty.eq(index).remove();
        })
    })
}
function removeClickBtn(btnsRemove){
    btnsRemove.each(function (){
        $(this).unbind('click');
    })
}


// edit product


// get list product by category
function getProductsByCate(){
    $('#ep-cate-select').change(function (){
        var cate = $('#ep-cate-select').val();
        var url = PATH_API + `/product/category/${cate}`
        $.get(url, function (response){
            showTableEditProduct(response);
        })
    })
}

// search product
function searchProduct(){
    var url = PATH_API + '/products/search'
    $.ajax({
        url: url,
        type: "GET",
        data: {
            keyword: $('#ep-search-input').val()
        }
    }).done(function (response){
        showTableEditProduct(response);
    })
}

// get list product
function listProduct(){
    var url = PATH_API + '/products';
    $.get(url, function (response){
    }).done(function (response){
        showTableEditProduct(response);
    })
}

// remove product
function removeProduct(){
    $('.ep-del-product-btn').each(function (){
        $(this).click(function (){
            DEL_CONFIRM();
            var productId = $(this).attr('product-id');
            $('#del-comfirm-btn').click(function (){
                var url = PATH_API + `/product/${productId}`
                $.ajax({
                    url: url,
                    type: 'DELETE'
                }).done(function (){
                    listProduct();
                })
            })
        })
    })
}

// view product detail
function viewProduct(){
    $('.ep-view-product-btn').each(function (){
        $(this).click(function (){
            var productId = $(this).attr('product-id');
            var url = PATH_API + `/product/${productId}`;
            $.get(url, function (response){

            }).done(function (response){
                showProductDetail(response);
            })
        })
    })
}

function showTableEditProduct(listProduct){
    $('#edit-product-table tbody').empty();
    var stt = 1;
 listProduct.forEach(product => {
     $('#edit-product-table tbody').append($('<tr>')
         .append($('<td>').append(stt++))
         .append($('<td>').append(`
            <a href="/product/${product.productId}" class="text-decoration-none d-flex text-black">
                <img src="${product.imageName}">
                <span class="fw-bold ps-2">
                    ${product.name}
                </span>
            </a>
         `))
         .append($('<td>').append(product.code))
         .append($('<td>').append(product.priceCurr))

         .append($('<td>').append(`
                <span product-id="${product.productId}" role="button" 
                    class="text-primary pe-2 ep-view-product-btn" 
                    data-bs-toggle="modal" data-bs-target="#productModal">Chi tiết</span>
                <span product-id="${product.productId}" role="button" 
                    class="text-danger ps-2 ep-del-product-btn">Xóa</span>
         `))
     )
 })
    removeProduct();
    viewProduct();
}

// show product detail to modal
function showProductDetail(product){
        console.log(product)
        $('#ep-product-code').text(product.code);
        $('#ep-product-name-input').val(product.name);
        $('#ep-product-price-input').val(product.price);
        $('#ep-product-table tbody').empty();
        $('#ep-update-product-infor').attr('product-id', product.productId);

        product.productQuantities.forEach(quantity => {
            var color = 'Không';
            if(quantity.color != null){
                color = quantity.color;
            }

            $('#ep-product-table tbody').append($('<tr>').addClass('ep-qty-item')
                .append($('<td>').addClass('text-uppercase').append(quantity.size))
                .append($('<td>').addClass('text-capitalize').append(color))
                .append($('<td>').append(quantity.quantity))
                .append($('<td>').append(`
                    <span role="button" class="text-primary ep-edit-qty-btn" data-bs-toggle="modal" data-bs-target="#ep-edit-qty-modal" 
                    quantity-id="${quantity.quantityId}">Sửa</span>
                `))
                .append($('<td>').append(`
                    <span role="button" class="text-danger ep-del-qty-btn" quantity-id="${quantity.quantityId}">Xóa</span>
                `))
            )
        })
    $('#ep-product-table tbody').append(`
                <tr>
                    <td><input type="text" id="ep-add-qty-size-input" className="form-control input w-50"></td>
                    <td><input type="text" id="ep-add-qty-color-input" className="form-control input w-50"></td>
                    <td><input type="number" id="ep-add-qty-qty-input" className="form-control input w-50"></td>
                    <td class="text-primary" product-id="${product.productId}" id="ep-add-qty-btn" role="button">Thêm</td>
                    <td></td>
                </tr>
    `)
    editProductInfor();
    editProductQty();
    removeProductQty();
    addProductQty();

}

function addProductQty(){
    $('#ep-add-qty-btn').click(function (){
        var size = $('#ep-add-qty-size-input').val();
        var color = $('#ep-add-qty-color-input').val();
        var qty = $('#ep-add-qty-qty-input').val();
        var productId = $(this).attr('product-id');
        var url = PATH_API + `/product/${productId}/quantity/`;
        dataJson = {
            size: size,
            color: color,
            quantity: qty
        }
        $.ajax({
            url: url,
            type: "POST",
            data: JSON.stringify(dataJson),
            contentType: 'application/json'
        }).done(function (){
            var url = PATH_API + `/product/${productId}`;
            $.get(url, function (response){

            }).done(function (response){
                showProductDetail(response);
            })
        })
    })
}

// edit product ìnor
function editProductInfor(){
    $('#ep-product-name-btn').click(function (){
        $('#ep-product-name-input').prop('readonly', '')
        $('#ep-product-name-input').removeClass('border-0')
        $('#ep-product-name-input').change(function (){
            $('#ep-product-name-input').addClass('border-0')
            $('#ep-product-name-input').prop('readonly', 'true')
        })
    })
    $('#ep-product-price-btn').click(function (){
        $('#ep-product-price-input').prop('readonly', '')
        $('#ep-product-price-input').removeClass('border-0')
        $('#ep-product-price-input').change(function (){
            $('#ep-product-price-input').addClass('border-0')
            $('#ep-product-price-input').prop('readonly', 'true')
        })
    })

}
function editProductQty(){
    $('.ep-edit-qty-btn').each(function (){
        $(this).prop('click');
    })
    $('.ep-edit-qty-btn').each(function (){
        $(this).prop('click');
        $(this).click(function (){

            let sizeElem = $(this).parents('.ep-qty-item').find("td:eq(0)");
            let colorElem = $(this).parents('.ep-qty-item').find("td:eq(1)");
            let qtyElem = $(this).parents('.ep-qty-item').find("td:eq(2)");

            console.log(sizeElem + colorElem + qtyElem)

            let size = sizeElem.text();
            let color = colorElem.text();
            let qty = qtyElem.text();

            $('#ep-edit-qty-size').val(size)
            $('#ep-edit-qty-color').val(color);
            $('#ep-edit-qty-qty').val(qty);

            $('#ep-update-qty-btn').click(function (){
                size = $('#ep-edit-qty-size').val()
                color = $('#ep-edit-qty-color').val();
                qty = $('#ep-edit-qty-qty').val();

                sizeElem.text(size);
                colorElem.text(color);
                qtyElem.text(qty);

                console.log(qty);

                $(this).unbind('click');
            })

        })

    })
}
function editSetQuantityProduct(sizeElem, colorElem, qtyElem){
    var size = $('#ep-edit-qty-size').val()
    var color = $('#ep-edit-qty-color').val();
    var qty = $('#ep-edit-qty-qty').val();

    sizeElem.text(size);
    colorElem.text(color);
    qtyElem.text(qty);
}

function removeProductQty(){
    $('.ep-del-qty-btn').each(function (){
        $(this).click(function (){
            DEL_CONFIRM();
            var quantityId = $(this).attr('quantity-id');
            var url = PATH_API + `/product-quantity/${quantityId}`
            var qtyElm = $(this);
            $('#del-comfirm-btn').click(function (){
                if(quantityId == undefined){
                    qtyElm.parents('.ep-qty-item').remove();
                }
                else{
                    $.ajax({
                        url: url,
                        type: "DELETE",
                        success: function (){
                            console.log('remove product qty')
                            qtyElm.parents('.ep-qty-item').remove();
                        }
                    })
                }
            })
        })
    })
}

// update product infor
function updateProductInfor(productId){

    var quantities = new Array();
    $('#ep-product-table tbody tr').each(function (){
        var quantityId = $(this).find('td:eq(3) span').attr('quantity-id');
        var size = $(this).find('td:eq(0)').text();
        var color = $(this).find('td:eq(1)').text();
        var quantity = $(this).find('td:eq(2)').text();

        if(quantityId == undefined){
            return false;
        }

        if(color == 'Không'){
            color = null
        }

        var dataJson = {
            quantityId: quantityId,
            size: size,
            color, color,
            quantity: quantity
        }

        quantities.push(dataJson);

    })
    var dataJson = {
        productId: productId,
        name: $('#ep-product-name-input').val(),
        price: $('#ep-product-price-input').val(),
        productQuantities: quantities
    }
    var url = PATH_API + `/product/${productId}/`;

    $.ajax({
        url: url,
        type: "PUT",
        data: JSON.stringify(dataJson),
        contentType: 'application/json'
    }).done(function (){
        listProduct();
    })
}