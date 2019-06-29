$(document).ready(function(){
    $('#get_jks').on('click', function(){
    	
    	var token = localStorage.getItem("token");
    	console.log(token);
    	
    	var xhr = new XMLHttpRequest();
    	xhr.open('GET', "/keystore/getjks", true);
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
    
    });
});