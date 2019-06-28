$(document).ready(function(){
	
	var token = localStorage.getItem("token");
	if (token == "undefined" || token == null || token == "null") {
		window.location.href = "index.html";
	} else {
	}
	
    $('#successModal').modal();

    $("#submitUdpload").click(function() {

    });
    
    $("#btn_log_out").click(function() {
    	localStorage.removeItem("token");
    	window.location.replace("index.html");
    });
});