$(document).ready(function(){
    $('#login_modal').modal();
    $('#signup_modal').modal();

    btnModalLogin = $('#login_modal_btn');
    txtLoginEmailInput = $('#login_email_input');
    txtLoginPasswordInput = $('#login_password_input');
    txtValidationErrorLogin = $("#validation_error_text_login");


    $('#open_login_modal').on('click', function(){
      txtLoginEmailInput.val("");
      txtValidationErrorLogin.text("");
      txtLoginPasswordInput.val("");
    });


    btnModalLogin.on("click", function(){
      if(txtLoginEmailInput.val() == ""){
        txtValidationErrorLogin.text("Please enter your email!");
      }else if(txtLoginPasswordInput.val() == ""){
        txtValidationErrorLogin.text("Please enter your password!");
      }else{
    	var data = {
    			'username':txtLoginEmailInput.val(),
    			'password':txtLoginPasswordInput.val()
    	}
    	console.log(data);
    	$.ajax({
    		type: 'POST',
    	    contentType: 'application/json',
    	    url: 'http://localhost:8080/authentication',
    	    data: JSON.stringify(data),
    	    dataType: 'json',
    	    crossDomain: true,
    	    cache: false,
    		processData: false,
    		success:function(response){
    			var token = response.token;
   
    			localStorage.setItem("token",token);
    				    				
    			window.location.href = "user.html";	
    		},
    			error: function (jqXHR, textStatus, errorThrown) {  
    				if(jqXHR.status=="401"){
    					alert("Wrong email or password.");
    				}
    			}
    		});
      }
    });

    txtSignupEmailInput = $('#signup_email_input');
    txtSignupPasswordInput = $('#signup_password_input');
    txtSignupRepeatPasswordInput = $('#signup_password_repeat_input');
    txtSignupValidationError = $('#validation_error_text_signup');

    $('#signup_modal_btn').on('click', function(){
      if(txtSignupEmailInput.val() == ""){
        txtSignupValidationError.text("Please enter your email!");
      }else if(txtSignupPasswordInput.val() == ""){
        txtSignupValidationError.text("Please enter your password!");
      }else if(txtSignupRepeatPasswordInput.val() == ""){
        txtSignupValidationError.text("Please repeat your password!");
      }else if(txtSignupPasswordInput.val() != txtSignupRepeatPasswordInput.val()){
        txtSignupValidationError.text("Passwords doesn't match!");
      }else{
        var params = {
          "email" : txtSignupEmailInput.val(),
          "password" : txtSignupPasswordInput.val()          
          }

          $.ajax({
            data: JSON.stringify(params),
            type: "POST",
            contentType: 'application/json',
            url: "user/createAcc",
            dataType: "json",
            success: function(data){
              txtSignupValidationError.text("Succesful registration, please wait for the administrator's approval.");
            }, 
            error: function (jqXHR, textStatus, errorThrown) {  
              if(jqXHR.status=="403"){
                txtSignupValidationError.text("Email already in use.");
              }
            }
          });

      }
    });

    $('#open_signup_modal').on('click', function(){
      txtSignupEmailInput.val("");
      txtSignupPasswordInput.val("");
      txtSignupRepeatPasswordInput.val("");
      txtSignupValidationError.text("");
    })

  });