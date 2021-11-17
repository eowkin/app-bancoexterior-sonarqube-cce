/**
 * Created by elporfirio on 24/05/17.
 */
$(document).ready(function(){
    var screen = $('#loading-screen');
    configureLoadingScreen(screen);

	var fecha = new Date(); //Fecha actual
  	var mes = fecha.getMonth()+1; //obteniendo mes
  	var dia = fecha.getDate(); //obteniendo dia
  	var ano = fecha.getFullYear(); //obteniendo año
  	if(dia<10)
   	 	dia='0'+dia; //agrega cero si el menor de 10
  	if(mes<10)
    	mes='0'+mes //agrega cero si el menor de 10
  	//document.getElementById('fechaDesde').value=ano+"-"+mes+"-"+dia;
  	//document.getElementById('fechaHasta').value=ano+"-"+mes+"-"+dia;
  	

    $('.do-request').on('click', function(){
         if($("#fechaDesde").val() != "" && $("#fechaHasta").val() != ""){
			//alert("Si lo llamo");
			 $.get('/ccetransacciones/cargarLoader')
             .done(function(){
                 
             })
             .fail(function(error){
                 console.error(error);
             })
		 }else{
			//alert("No lo llamo");
		 }
         
         
        
    })
});

function configureLoadingScreen(screen){
    $(document)
        .ajaxStart(function () {
            screen.fadeIn();
           
        })
        
}



