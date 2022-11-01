$('#reset-btn').click(function (){
    hideFeedback();
    var email = $('#reset-email').val();
    if (email == ''){
        showFeedback("Nhập đầy đủ thông tin", 0);
    }
    else if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(email) == false){
        showFeedback("Email không hợp lệ", 0)
    }
    else{
        var url = PATH_API + `/user/forgetpass`;
        showSpinnerLoading();
        $.ajax({
            url: url,
            type: 'GET',
            data: {
                email: email,
            }
        }).done(function (response){
            hideSpinnerLoadin();
            if (response == false)
                showFeedback("Email không tồn tại.", 0);
            else
                showFeedback("Mật khẩu mới đã được gửi tới Email của bạn!", 1);
        })
    }

})

function showSpinnerLoading(){
    $("#spinner-loading").prop("hidden", false);
}
function hideSpinnerLoadin(){
    $("#spinner-loading").prop("hidden", true);
}
function showFeedback(message, type){
    var feedbackElm = $('#feedback');
    feedbackElm.removeClass();
    feedbackElm.text(message)
    if (Number(type) == 0)
        feedbackElm.addClass("text-danger");
    else
        feedbackElm.addClass("text-success");

    feedbackElm.prop("hidden", false);
}

function hideFeedback(){
    $('#feedback').prop("hidden", true);
}
