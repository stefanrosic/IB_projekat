$(document).ready(function(){
	$("#users").hide();

	var token = localStorage.getItem("token");
	if (token == "undefined" || token == null || token == "null") {
		window.location.href = "index.html";
	} else {
	}
	
    getData();
            
    $.ajax({
    	url:'https://localhost:8443/user/getCurrentRole',
    	headers:{Authorization:"Bearer " + token},
    	type: 'GET',
    	dataType:'json',
    	crossDomain: true,
    	success:function(response){
    		for(var i=0; i<response.length; i++){
    			var authority = response[i];

    			if(authority.name=="ADMIN"){
        			$("#users").show();
    			}
    		}
    	},
    	error: function (jqXHR, textStatus, errorThrown) {
    		console.log('GRESKA')
    	}
    });
    
    $.ajax({
    	url:'https://localhost:8443/user/getAllFiles',
    	headers:{Authorization:"Bearer " + token},
    	type: 'GET',
    	dataType:'json',
    	crossDomain: true,
    	success:function(response){
    		for(var i=0; i<response.length; i++){
    			var file = response[i];
    			console.log(file)
    			$('#uploads').append('<button type="button" Value="'+file+'"class="btn" id="us"><b>'+file+'</b></button>')
    		}
    	},
    	error: function (jqXHR, textStatus, errorThrown) {
    		console.log('GRESKA')
    	}
    });
    
    
	$(document).on("click","#uploads #us", function(e) {
		value = $(this).val();
		downloadFile(value);
	});

	function downloadFile(file){
		var token = localStorage.getItem("token");
		console.log(token);
		
		var xhr = new XMLHttpRequest();
		xhr.open('GET', "user/download/"+file, true);
		xhr.setRequestHeader("Authorization", "Bearer " + token);
		xhr.responseType = 'blob';

		xhr.onload = function(e) {
			if (this.status == 200) {
				var blob = this.response;
				console.log(blob);
				var a = document.createElement('a');
				var url = window.URL.createObjectURL(blob);
				a.href = url;
				a.download = xhr.getResponseHeader('filename');
				a.click();
				window.URL.revokeObjectURL(url);
			}
		};

		xhr.send();
	}

    $(document).on("click","#tab tbody tr td button", function() { // any button
        
    	$.ajax({
    		url:'https://localhost:8443/user/activate/' + $(this).val(),
    		headers:{Authorization:"Bearer " + token},
    		type: 'POST',
    		dataType:'json',
    		crossDomain: true,
    		success:function(response){
                getData();
    		},
    		error: function (jqXHR, textStatus, errorThrown) {
	    		getData();
    		}
    	});

    });

function getData(){
	var token = localStorage.getItem("token");
	$.ajax({
		url:'https://localhost:8443/user/getAll',
		headers:{Authorization:"Bearer " + token},
		type: 'GET',
		dataType:'json',
		crossDomain: true,
		success:function(response){
	        $('table tbody').empty();
	        for(var i=0; i<response.length; i++){
	        	var user = response[i];
	            var id = user.id;
	            var email = user.email;
	            var authority = user.authority.name;
	            var activated = user.active;
	            var certificate = user.certificate;
	            var button;
	            if(activated === true){
	                button = "<button value = '"+id+"' id = 'btnActivate' class='waves-effect waves-light btn red'>Deactivate</button>";
	            }else{
	                button = "<button value = '"+id+"' id = 'btnActivate' class='waves-effect waves-light btn light-green accent-3'>Activate</button>";
	            }

	            var markup = "<tr><td>" + email + "</td><td>"+ authority +"</td><td>"+certificate+"</td><td>"+activated+"</td><td>"+button+"</td></tr>";


	            $('table tbody').append(markup);
	        }
		},
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus+" "+jqXHR.status)
		}
	});
}
		
	$("#btn_log_out").click(function() {
		localStorage.removeItem("token");
		window.location.replace("index.html");
	});
});
