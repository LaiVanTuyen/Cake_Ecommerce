const PATH_API = '/api/v1';
const DEL_CONFIRM = function (){
    $('#del-confirm-modal').modal('show');
};

$(document).ready(function (){
  // checkRole();
  showCartQuantity();
  $('#needs-validation').bootstrapValidator();
});

let mybutton = document.getElementById("btn-back-to-top");
window.onscroll = function () {
  scrollFunction();
};

function scrollFunction() {
  if (
      document.body.scrollTop > 20 ||
      document.documentElement.scrollTop > 20
  ) {
    mybutton.style.display = "block";
  } else {
    mybutton.style.display = "none";
  }
}
// When the user clicks on the button, scroll to the top of the document
mybutton.addEventListener("click", backToTop);

function backToTop() {
  document.body.scrollTop = 0;
  document.documentElement.scrollTop = 0;
}

// switch form
//account
// let headerLink = document.querySelector('.header-link')
// console.log(headerLink.textContent);
// if(headerLink.textContent.trim() != ''){
//   console.log('asdf');
//   var topRights = document.querySelectorAll('.topRight')
//   for(let i=0; i<topRights.length; i++){
//     topRights[i].classList.toggle('object--disapper')
//   }
// }

//validation form
(function () {

  var forms = document.querySelectorAll('.needs-validation')

  Array.prototype.slice.call(forms)
      .forEach(function (form) {
        form.addEventListener('submit', function (event) {
          if (!form.checkValidity()) {
            event.preventDefault()
            event.stopPropagation()
          }

          form.classList.add('was-validated')
        }, false)
      })
})()


function checkRole(){
  if($('.topRight').children('a').length == 3){
      var url = PATH_API + '/user/role';
      $.get(url, function (response){
        if(response[0].authority == 'ROLE_ADMIN'){
          $('.topRight').first().prepend(
              '<a href="/manage" class="header-link">\n' +
              '    <i class="fa-solid fa-briefcase"></i>\n' +
              '    Quản lý\n' +
              '</a>'
          )
        }
      })
  }

}

function showCartQuantity(){
    var username = document.querySelector('.header-link').textContent.trim();
    console.log(username);
    if(username != ''){
      var url = PATH_API + `/cart/${username}/quantity`;
      $.get(url, function (res){
        $('#cart-quantity').text(res);
      })
    }
    else{
      document.cookie.split(';').forEach(data => {
        if(data.split('=')[0].trim().localeCompare("cartItem") == 0){
          var value = data.split('=')[1];
          var size = 0;
          value.split('and').forEach(data => {
              if(data.trim() != ''){
                  size += Number(1);
              }
          });
          $('#cart-quantity').text(size);
        }
      })
    }
}