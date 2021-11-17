/**
 * Created by elporfirio on 24/05/17.
 */
$(document).ready(function(){
    var screen = $('#loading-screen');
    configureLoadingScreen(screen);

    $('.do-request').on('click', function(){
         $.get('/ccetransacciones/cargarLoader')
             .done(function(){
                 
             })
             .fail(function(error){
                 console.error(error);
             })
    })
});

function configureLoadingScreen(screen){
    $(document)
        .ajaxStart(function () {
            screen.fadeIn();
           
        })
        
}



