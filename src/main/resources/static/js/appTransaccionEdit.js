/**
 * Created by elporfirio on 24/05/17.
 */
$(document).ready(function(){

	var screen = $('#loading-screen');
    configureLoadingScreen(screen);
	
	
	
	$("#cuentaReceptor").bind("cut copy paste", function(e){
		e.preventDefault();
	});
	
	$("#montoString").bind("cut copy paste", function(e){
		e.preventDefault();
	});
	 
	 $("#bancoReceptor").change(function(e){
		e.preventDefault();
		//$("#cuentaBcvReceptor").val("Seleccionar");
		if($("#bancoReceptor").val() == ""){
			$("#cuentaBcvReceptor").val("Seleccionar");
	 	}else{
			getNroCuenta();
		
		}
	});	
		
	/** 
	$("#fmrDatos").submit(function(e){
		e.preventDefault();
		let monto = $("#monto").val();
		
		monto = parseFloat(monto);
		alert(monto);
		if(!monto){
			alert("algo va mal");
			return;
		}
		
	});*/
	  
    
});

function getNroCuenta(){
   codigoBic = $("#bancoReceptor").val();
   //alert(codigoBic); 
   urlString = "http://localhost:7090/site/cuentasUnicasBcvRest/"+codigoBic;
   //urlStringPrueba = "[[@{'/cuentasUnicasBcvRest/'}]]";
   //alert(urlStringPrueba);
   
   $.get(urlString, function(data){
		 cuentaBcvReceptor = $("#cuentaBcvReceptor");
	   console.log(data);
	   //var obj = responseJson;
	   $("#cuentaBcvReceptor").val(data.cuenta);
	   $("#cuentaBcvReceptorHidden").val(data.cuenta);	
	}).fail(function(){
			alert("error al conectarse al servidor");
		});
   
   
        
}

//Funcion solo para admitir Letras
function soloLetras(e){
	//keyCode which para eventos de mouse o teclado, esta propiedad 
	//indica la tecla o un especifico que se presiono.
	var key = e.keyCode || e.which;
	
	//fromCharCode Convierte un numero Unicode en un caracter
	//toLowerCase Convierte la cadena en minuscula
	var tecla = String.fromCharCode(key).toLowerCase();
	var letras =" áéíóúabcdefghijklmnñopqrstuvwxyz";
	
	//Arreglo que contiene los valores de las teclas
	//que queremos que no se muestren 
	var especiales = [8, 37, 39, 46];
	var tecla_especial = false;
	
	//Declaramos un ciclo que toma los elementos del arreglo especiales
	for(var i in especiales){
		/*Evaluamos la variable key si es igual a lo que esta almacenado
		en el arreglo especiales dependiendo a la posicion que esta 
		el ciclo for*/
		if(key == especiales[i]){
			tecla_especial = false;
			break;
		}
	}
	
	/**El Metodo indexOf() devuelve la posicion de la primera aparicion  
	de un valor especificado en una cadena.
	Este metodo devuelve -1 si el valor de busqueda nunca ocurre*/
	if(letras.indexOf(tecla) == -1 && !tecla_especial)
		return false;
	
	
}


//Funcion solo para admitir Numeros
function soloNumeros(e){
	//keyCode which para eventos de mouse o teclado, esta propiedad 
	//indica la tecla o un especifico que se presiono.
	var key = e.keyCode || e.which;
	
	//fromCharCode Convierte un numero Unicode en un caracter
	//toLowerCase Convierte la cadena en minuscula
	var tecla = String.fromCharCode(key).toLowerCase();
	var numeros = "0123456789";
	
	//Arreglo que contiene los valores de las teclas
	//que queremos que no se muestren 
	var especiales = [8, 37, 39, 46];
	var tecla_especial = false;
	
	//Declaramos un ciclo que toma los elementos del arreglo especiales
	for(var i in especiales){
		/*Evaluamos la variable key si es igual a lo que esta almacenado
		en el arreglo especiales dependiendo a la posicion que esta 
		el ciclo for*/
		if(key == especiales[i]){
			tecla_especial = false;
			break;
		}
	}
	
	/**El Metodo indexOf() devuelve la posicion de la primera aparicion  
	de un valor especificado en una cadena.
	Este metodo devuelve -1 si el valor de busqueda nunca ocurre*/
	if(numeros.indexOf(tecla) == -1 && !tecla_especial)
		return false;
	
	
}


//Funcion solo para admitir Numeros y Punto Decimal
function soloNumerosPunto(e){
	//keyCode which para eventos de mouse o teclado, esta propiedad 
	//indica la tecla o un especifico que se presiono.
	var key = e.keyCode || e.which;
	
	//fromCharCode Convierte un numero Unicode en un caracter
	//toLowerCase Convierte la cadena en minuscula
	var tecla = String.fromCharCode(key).toLowerCase();
	var numeros = "0123456789";
	
	//Arreglo que contiene los valores de las teclas
	//que queremos que no se muestren 
	var especiales = [46];
	var tecla_especial = false;
	
	//Declaramos un ciclo que toma los elementos del arreglo especiales
	for(var i in especiales){
		/*Evaluamos la variable key si es igual a lo que esta almacenado
		en el arreglo especiales dependiendo a la posicion que esta 
		el ciclo for*/
		if(key == especiales[i]){
			tecla_especial = true;
			break;
		}
	}
	
	/**El Metodo indexOf() devuelve la posicion de la primera aparicion  
	de un valor especificado en una cadena.
	Este metodo devuelve -1 si el valor de busqueda nunca ocurre*/
	if(numeros.indexOf(tecla) == -1 && !tecla_especial)
		return false;
	
	
}

function configureLoadingScreen(screen){
    $(document)
        .ajaxStart(function () {
            screen.fadeIn();
    }).ajaxStop(function () {
        screen.fadeOut();
    });
        
}



