var Login = function () {
    
    return {
    	register:function(){
    		 $('#registerForm').validate({
 	            errorElement: 'label', //default input error message container
 	            errorClass: 'help-inline', // default input error message class
 	            focusInvalid: false, // do not focus the last invalid input
 	            ignore: "",
 	            rules: {
 	                username: {
 	                    required: true,
 	                    rangelength:[5,20]
 	                },
 	                password: {
 	                    required: true,
 	                   rangelength:[5,20]
 	                },
 	                rpassword: {
 	                    equalTo: "#register_password"
 	                },
 	                email: {
 	                    required: true,
 	                    email: true
 	                },
 	                tnc: {
 	                    required: true
 	                }
 	            },

 	            messages: { // custom messages for radio buttons and checkboxes
 	            	username:{
 	            		required:'请输入用户名',
 	            		rangelength : '请输入 {0} 到 {1} 长度的用户名.' 	            		
 	            	},
 	            	password:{
 	            		required:'请输入密码',
 	            		rangelength : '请输入 {0} 到 {1} 长度的密码.'
 	            	},
 	            	rpassword:{
 	            		equalTo:'两次密码输入不一致'
 	            	},
 	                tnc: {
 	                    required: "请阅读并同意用户注册协议."
 	                }
 	            },

 	            invalidHandler: function (event, validator) { //display error alert on form submit   

 	            },

 	            highlight: function (element) { // hightlight error inputs
 	                $(element)
 	                    .closest('.control-group').addClass('error'); // set error class to the control group
 	            },

 	            success: function (label) {
 	                label.closest('.control-group').removeClass('error');
 	                label.remove();
 	            },

 	            errorPlacement: function (error, element) {
 	                if (element.attr("name") == "tnc") { // insert checkbox errors after the container                  
 	                    error.addClass('help-small no-left-padding').insertAfter($('#register_tnc_error'));
 	                } else {
 	                    error.addClass('help-small no-left-padding').insertAfter(element.closest('.input-icon'));
 	                }
 	            }
 	        });
    	},
        //main function to initiate the module
        init: function () { 	
           $('#loginForm').validate({
	            errorElement: 'label', //default input error message container
	            errorClass: 'help-inline', // default input error message class
	            focusInvalid: false, // do not focus the last invalid input
	            rules: {
	                username: {
	                    required: true
	                },
	                password: {
	                    required: true
	                }
	            },
	            messages: {
	                username: {
	                    required: "请输入邮箱/用户名/已验证手机."
	                },
	                password: {
	                    required: "请输入密码."
	                }
	            },

	           /* invalidHandler: function (event, validator) { //display error alert on form submit   
	                $('.alert-error', $('.login-form')).show();
	            },*/

	            highlight: function (element) { // hightlight error inputs
	                $(element).closest('.control-group').addClass('error'); // set error class to the control group
	            },
	            success: function (label) {
	                label.closest('.control-group').removeClass('error');
	                label.remove();
	            },
	            errorPlacement: function (error, element) {
	                error.addClass('help-small no-left-padding').insertAfter(element.closest('.input-icon'));
	            }
	        });

	        $('#loginForm input').keypress(function (e) {
	            if (e.which == 13) {
	                if ($('#loginForm').validate().form()) {
	                   $('#loginForm').submit();
	                }
	                return false;
	            }
	        });     
        }
    };

}();