$(document).ready(function(){
    $('.header-item').each(function(index, element){
        $(element).click(function(){
            switchOption(index, $(element));
        })
    })
})
function switchOption(index, element){

    $('.header-item').each(function(){
        $(this).removeClass('object--border-bottom');
    })
    element.addClass('object--border-bottom')

    $('.body-item').addClass('object--disapper');
    $('.body-item').eq(index).removeClass('object--disapper');
}