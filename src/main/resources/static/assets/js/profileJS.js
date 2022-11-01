const updateUserInforFrm = $("#updateUserInfor");
const changePassForm = $("#changePassFrm");

$(document).ready(function (){

    updateUserInforFrm.submit(function (e){
        e.preventDefault();
    });
    $('#addAddress-frm').submit(function (e){
        e.preventDefault();
    })
    changePassForm.submit(function (e){
        e.preventDefault();
    })
    $('#change-address-frm').submit(function (e){
        e.preventDefault();
    })

    $('.btn-changePass').click(function (e){
        updatePass();
        $("#changePassModal").modal('toggle');
    });

    $("#submitUpdateInfor").click(function (e){
        $.when(function (){
            setTimeout(1000);
        }).then(function (){
            if($("#valid-infor").css('display') == 'none') {
                updateUserInfor();
                $("#changeInfoModal").modal('toggle');
            }
        })
    })

    readyRemoveAddress();
    readyUpdateAddress();
    cancelOrder();
    // loadOrder();
});

//update user's password
function updatePass(){
    var pass = $("#password").val();
    var userId = changePassForm.attr("value");
    var url = PATH_API + `/user/changepass/`;
    var jsonData = {
        userId: userId,
        password: pass
    }
    console.log(jsonData)
    $.ajax({
        url: url,
        type: "POST",
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (){
        alert('DONE');
    })
}
//update user infor
function updateUserInfor(){
    var userId = updateUserInforFrm.attr("value");
    url = PATH_API + `/user/infor/${userId}/`;
    var inputs = $("#updateUserInfor input");
    var jsonData = {
        userId: userId,
        fullname: inputs.first().val(),
        phoneNumber: inputs.last().val(),
    };
    $.ajax({
        type: "POST",
        url: url,
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (){
        loadUserInfor();
    }).fail(function (){
    });
}

function loadUserInfor(){
    var userId = updateUserInforFrm.attr("value");
    url = PATH_API + `/user/${userId}/infor`;
    $.get(url, function (userInfor){
        $("#fullname-user").text(userInfor.fullname);
        $("#phonenumber-user").text(userInfor.phoneNumber);
    }).done(function (){

    }).fail(function (){

    });
}

// verify pass
function passwordVerify(value){
    console.log("HERE");
    var userId = changePassForm.attr("value");
    var url = PATH_API + `/user/checkpass`
    $.get({
        url: url,
        data: {
            id: userId,
            password: value
        },
        success:function (response){
            var btnSubmit = document.querySelector('.btn-changePass');
            if(response == false){
                $('.verifyPass-feedback').css('display', 'block');
            }
            else {
                $('.verifyPass-feedback').css('display', 'none');
            }
        },
        error:function (response){
        },
    })
}
// check confirm password
function checkChangePass(){
    var pass = document.getElementById("password").value;
    var confirmPass = document.getElementById("passwordComfirm").value;
    var feedback = document.querySelector(".changePass-feedback");
    if(pass.length < 6){
        feedback.classList.add('text-danger');
        feedback.innerText = 'Độ dài tối thiểu 6 ký tự';
    }
    else if(pass != confirmPass){
        feedback.innerText = "Mật khẩu không trùng nhau";
    }
    else{
        feedback.style.display = "none";
    }
}

function checkVerifySub(){
    var btnSubmid = document.querySelector('.btn-changePass');
    if($('.changePass-feedback').css('display') == 'none' && $('.verifyPass-feedback').css('display')=='none'){
        console.log('Heree');
        btnSubmid.disabled = false;
    }
}

// add address
function addAdress(){
    console.log("HERE")
    $.when(function (){
        setTimeout(500);
    }).then(function (){
        if($('#valid-address').css('display') == 'none'){
            var url = PATH_API + `/address/`;
            var dataJson = {
                province: $('#province-select-1').val().split('-')[1],
                district: $('#district-select-1').val().split('-')[1],
                ward: $('#ward-select-1').val(),
                detail: $('#address-detail').val(),
                receiverName: $('#address-receiver').val(),
                phoneNumber: $('#address-phone').val(),
                userId: $('.profile-address').attr('user-id')
            }
            console.log(dataJson)
            $('#addAddress-modal').modal('toggle');
            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(dataJson),
                contentType: 'application/json'
            }).done(function (){
                loadAddress();
            })
        }
    })
}
function loadAddress(){
    var userId = $('.profile-address').attr('user-id');
    var addressTable = $('#address-table');
    addressTable.children('tbody').empty();
    var url = PATH_API + `/user/${userId}/address`;
    $.get(url, function (listAddress){
        listAddress.forEach(address => {
            addressTable.append(($('<tr>').attr('address-id', address.addressId))
                .append($('<td>').addClass('ps-0').append(address.receiverName))
                .append($('<td>').append(address.detail))
                .append($('<td>').append(address.ward))
                .append($('<td>').append(address.district))
                .append($('<td>').append(address.province))
                .append($('<td>').append(address.phoneNumber))
                .append($('<td>')
                    .append('<span role="button" class="text-primary me-2" data-bs-toggle="modal" data-bs-target="#changeAddressModal">Sửa</span>\n' +
                            '<span role="button" class="text-decoration-none text-danger">Xóa</span>'))
            )
        })
    }).done(function (){
        readyRemoveAddress();
        readyRemoveAddress();
    });
}

//remove adddres
function readyRemoveAddress(){
    var rows = $('#address-table').children('tbody').find("tr");
    rows.each(function (index, eleRow){
        var btn = $(eleRow).children('td').children('span').last();
        btn.click(function (){
            addressId = $(eleRow).attr('address-id');
            console.log(addressId, "HERE");
            removeAddress(addressId);
        })
    });
};
function removeAddress(addressId){
    url = PATH_API + `/address/${addressId}`
    $.ajax({
        url: url,
        type: "DELETE"
    }).done(function (){
       loadAddress();
    });
}

// update address
function readyUpdateAddress(){
    var rows = $('#address-table').children('tbody').find('tr');
    rows.each(function (index, eleRow){
       var subBtn = $(eleRow).children('td').children('span').first();
       subBtn.click(function (){
            var addressId = $(eleRow).attr('address-id');
            var url = PATH_API + `/address/${addressId}`;
            $.get(url, function (response){
                loadAddressToModal(addressId, response);
            })
       })
    });
}
function loadAddressToModal(addressId, jsonData){
    var inputs = $('#change-address-frm').children('div').children('input');
    inputs.first().val(jsonData.receiverName);
    inputs.eq(1).val(jsonData.detail);
    inputs.last().val(jsonData.phoneNumber);
    $('.btn-changeAddress').click(function (){
        $.when(function (){
            setTimeout(1000);
        }).then(function (){
            if($('#valid-change-address').css('display') == 'none'){
                updateAddress(addressId);
                $('#changeAddressModal').modal('toggle');
            }
        })
    })
}
function updateAddress(addressId){
    var inputs = $('#change-address-frm').children('div').children('input');
    var url = PATH_API + `/address/${addressId}/`;
    jsonData = {
        receiverName: inputs.first().val(),
        detail: inputs.eq(1).val(),
        ward: $('#ward-select').val(),
        district: $('#district-select').val().split('-')[1],
        province: $('#province-select').val().split('-')[1],
        phoneNumber: inputs.last().val()
    }
    $.ajax({
        url: url,
        type: 'PUT',
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (){
        loadAddress()
    });
}

//cancel order
function cancelOrder(){
    $('.order-cancel').each(function (){
        $(this).click(function (){
            var orderId = $(this).attr('order-id');
            var url = PATH_API + `/order/${orderId}`;
            $.ajax({
                url: url,
                type: "POST",
                data: {
                    action: 2
                },
                dataType: 'json'
            }).done(showOrderTable($(this)));
        })
    })
}

function showOrderTable(element){
    var status = $(element).parents(".order-item").find(".order-status")
    element.css('cursor', 'not-allowed');
    status.removeClass();
    status.addClass('text-danger');
    status.text('Đã hủy')
}