$(document).ready(function(){
    $('#get_jks').on('click', function(){
      
        // $.get('http://localhost:8080/keystore/getjks',{"id" : 2}, function(){
        // 	alert('ok');
        // });
    
        $.ajax({
            url: 'http://localhost:8080/keystore/getjks',
            type: 'GET',
            data: {"id" : 2},
            success: function(res, status, xhr) { 
                window.location = 'boriss_jks.jks';
              },
              download: function(){
                  alert('ok');
              }
        });
    
    });
});