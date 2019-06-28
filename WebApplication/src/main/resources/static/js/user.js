$(document).ready(function(){

	var token = localStorage.getItem("token");
	if (token == "undefined" || token == null || token == "null") {
		window.location.href = "index.html";
	} else {
	}
	
    getData();
    

    $(document).on("click","#tab tbody tr td button", function() { // any button
        //alert($(this).val());
        var params = {
            "id" : $(this).val()
        }
         $.post("user/activate", params, function () {
             getData();
         });


    });

function getData(){
    $.get("user/getAll", function(data){
        var users = JSON.parse(data);
        console.log(users);
        $('table tbody').empty();
        for(var i = 0; users.length ; i++){
            var id = users[i].id;
            var email = users[i].email;
            var authority = users[i].authority.name;
            var activated = users[i].active;
            var certificate = users[i].certificate;
            var button;
            if(activated === true){
                button = "<button value = '"+id+"' id = 'btnActivate' class='waves-effect waves-light btn red'>Deactivate</button>";
            }else{
                button = "<button value = '"+id+"' id = 'btnActivate' class='waves-effect waves-light btn light-green accent-3'>Activate</button>";
            }

            var markup = "<tr><td>" + email + "</td><td>"+ authority +"</td><td>"+certificate+"</td><td>"+activated+"</td><td>"+button+"</td></tr>";


            $('table tbody').append(markup);
        }

    });
    

}
$("#btn_log_out").click(function() {
	localStorage.removeItem("token");
	window.location.replace("index.html");
});
});
