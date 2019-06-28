$(document).ready(function(){
	
	var token = localStorage.getItem("token");
	console.log(token + "         ------------------------------")
	if (token == "undefined" || token == null || token == "null") {
		window.location.href = "index.html";
	} else {
	}
	
    $('#successModal').modal();

    $("#submitUdpload").click(function() {

    });
});