var Admin = {

  toggleLoginRecovery: function(){
    var is_login_visible = $('#modal-login').is(':visible');
    (is_login_visible ? $('#modal-login') : $('#modal-recovery')).slideUp(300, function(){
      (is_login_visible ? $('#modal-recovery') : $('#modal-login')).slideDown(300, function(){
        $(this).find('input:text:first').focus();
      });
    });
  }
   
};

$(function(){

  $('.toggle-login-recovery').click(function(e){
    Admin.toggleLoginRecovery();
    e.preventDefault();
  });

});

function getBaseURL() {
    var base_url = window.location.origin;

    return base_url;
}


function isUndifined(variable) {
    if (typeof variable == 'undefined' || variable == 'undefined') { // Any scope
        return true;
    }
    return false;
}