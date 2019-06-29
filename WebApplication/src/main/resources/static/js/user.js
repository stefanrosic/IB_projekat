$(document).ready(function(){

	var token = localStorage.getItem("token");
	if (token == "undefined" || token == null || token == "null") {
		window.location.href = "index.html";
	} else {
	}
	
    getData();
    

    $(document).on("click","#tab tbody tr td button", function() { // any button
        
    	$.ajax({
    		url:'http://localhost:8080/user/activate/' + $(this).val(),
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
		url:'http://localhost:8080/user/getAll',
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
