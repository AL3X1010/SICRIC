/*  Nombre.....:  Utilities.js
 *  Descripción:  Contiene funciones de utilidad para las páginas web.
 *  Autor......:  Allan Ponce + Moisés Torres
 *  Fecha......:  Junio, 2019.
 */

// * * * * *   Eventos de JQuery   * * * * *
var errorSesion = false;

// Finaliza la sesión actual.
$(document.body).on("click", ".btnSalir", function () {
    $.ajax({
        method: "post",
        async: false,
        url: "serviciosc.do",
        data: {name: "CLOSSN"},
        success: function (data, status) {
            var respuestaServ = $.trim(data.toString());

            if (respuestaServ === 'true') {
                window.location.replace("index.html");
            }
        }
    });
});


// Actualiza los elementos SELECT-html.
$('select').on('contentChanged', function () {
    $(this).formSelect();
});


// Limita los caracteres del campo en cuestión, solo números y un punto.
$('.campoDecimal').keypress(function (event) {
    var code = (event.keyCode ? event.keyCode : event.which);
    if (!((code >= 48 && code <= 57) //numbers
            || (code === 46)) //period
            || (code === 46 && $(this).val().indexOf('.') !== -1))
        event.preventDefault();
});


// Limita los caracteres del campo en cuestión, solo números.
// $('.campoEntero').keypress(function (event) {
$(document.body).on("keypress", ".campoEntero", function(event){
    var code = (event.keyCode ? event.keyCode : event.which);
    if (!((code >= 48 && code <= 57))
            || (code === 46 && $(this).val().indexOf('.') !== -1))
        event.preventDefault();
});


// Limita los caracteres del campo en cuestión, solo letras y números.
$('.campoLetrasNumeros').keypress(function (e) {
    var regex = new RegExp("^[a-zA-Z0-9]+$");
    var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);

    if (regex.test(str))
        return true;

    e.preventDefault();
    return false;
});


$('.campoMayusculas').keyup(function () {
    $(this).val($(this).val().replace(/\W/g, '').toUpperCase());
});


// Limita los caracteres del campo en cuestión, solo letras y espacios.
$('.campoLetrasEspacios').keypress(function (e) {
    var regex = new RegExp("^[a-zA-Z ]+$");
    var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);

    if (regex.test(str))
        return true;

    e.preventDefault();
    return false;
});


// Limita que no se pueda digitar ningún caracter en el campo.
$('.campoVacio').keypress(function (e) {
    var regex = new RegExp("^[]+$");
    var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);

    if (regex.test(str))
        return true;

    e.preventDefault();
    return false;
});


// Registra la salida de la página web para rastreo de la sesión.
window.onbeforeunload = function () {
    $.ajax({
        method: "post",
        url: "serviciosc.do",
        data: { name: "SAVTRCINF",
                page: document.location.href.match(/[^\/]+$/)[0] },
        success: function (data, status) {}
    });
};


// * * * * *   Funciones utilizadas por páginas web.   * * * * *
// Función que muestra los elementos de la página para lectura o edición.
function adecuarElementos(modo) {
    var MODO_EDICION = 'mod-e';
    var MODO_VISUALIZACION = 'mod-v';

    if (modo === MODO_EDICION) {
        $('input').removeAttr('readonly');
        $('input').removeAttr('disabled');
        $('textarea').removeAttr('readonly');
        $('select').removeAttr('disabled');
        $('.rbtFrecuencia').removeAttr('disabled');
        $('#btnRegresar').removeClass('right');
        $('#btnSubmit').css('visibility', 'visible');
        $('#cbxActivo').removeAttr('disabled');
        $('#icon').text('mode_edit').css({'padding-top': '1%', 'color': '#ff5722'});
        $('#nombreArchivo').css('visibility', 'visible');
        $('.timepicker').timepicker({
                    twelveHour: false,
                    i18n: {
                        done: "OK",
                        cancel: "Cancelar"
                    }
                });
    } else if (modo === MODO_VISUALIZACION) {
        $('form').removeAttr('action');
        $('input').attr('disabled', 'disabled');
        $('input').removeClass('validate');
        $('textarea').attr('disabled', 'disabled');
        $('textarea').removeClass('validate');
        $('select').attr('disabled', 'disabled');
        $('.datepicker').datepicker('destroy');
        $('.timepicker').timepicker('destroy');
        $('.rbtFrecuencia').attr('disabled', 'disabled');
        $('#btnSubmit').css('visibility', 'hidden');
        $('#cbxActivo').attr('disabled', 'disabled');
        $('#icon').text('remove_red_eye').css('padding-top', '1.2%');
        $('#nombreArchivo').css('visibility', 'hidden');
    }
}


// Función que convierte caracteres especiales UNICODE.
function caracteresEspeciales(texto) {
    var INICIO_ADMIRACION = "¡";
    var A_MAYUSCULA_TILDADA = "Á";
    var E_MAYUSCULA_TILDADA = "É";
    var I_MAYUSCULA_TILDADA = "Í";
    var O_MAYUSCULA_TILDADA = "Ó";
    var U_MAYUSCULA_TILDADA = "Ú";
    var N_MAYUSCULA_TILDADA = "Ñ";
    var A_MINUSCULA_TILDADA = "á";
    var E_MINUSCULA_TILDADA = "é";
    var I_MINUSCULA_TILDADA = "í";
    var O_MINUSCULA_TILDADA = "ó";
    var U_MINUSCULA_TILDADA = "ú";
    var N_MINUSCULA_TILDADA = "ñ";

    texto = texto.replace(/\\u00A1/g, INICIO_ADMIRACION);
    texto = texto.replace(/\\u00C1/g, A_MAYUSCULA_TILDADA);
    texto = texto.replace(/\\u00C9/g, E_MAYUSCULA_TILDADA);
    texto = texto.replace(/\\u00CD/g, I_MAYUSCULA_TILDADA);
    texto = texto.replace(/\\u00D3/g, O_MAYUSCULA_TILDADA);
    texto = texto.replace(/\\u00DA/g, U_MAYUSCULA_TILDADA);
    texto = texto.replace(/\\u00D1/g, N_MAYUSCULA_TILDADA);
    texto = texto.replace(/\\u00E1/g, A_MINUSCULA_TILDADA);
    texto = texto.replace(/\\u00E9/g, E_MINUSCULA_TILDADA);
    texto = texto.replace(/\\u00ED/g, I_MINUSCULA_TILDADA);
    texto = texto.replace(/\\u00F3/g, O_MINUSCULA_TILDADA);
    texto = texto.replace(/\\u00FA/g, U_MINUSCULA_TILDADA);
    texto = texto.replace(/\\u00F1/g, N_MINUSCULA_TILDADA);

    return texto;
}


// Función que verifica si existe sesión creada.
function establecerCodigo(paginaWeb, codigo, modo) {
    if (codigo === undefined)
        codigo = -1;
    if (modo === undefined)
        modo = "mod-v";

    $.ajax({
        method: "post",
        async: false,
        url: "serviciosc.do",
        data: {name: "SETCDE",
            page: paginaWeb.toString(),
            code: codigo,
            mode: modo},
        success: function (data, status) {
            var respuestaServ = JSON.parse($.trim(data.toString()));
        }
    });
}


// Función que cambia DD/MM/YYYY a YYYYMMDD.
function formatearFecha(fechaTexto) {
    if (fechaTexto === "")
        return 0;

    var anio = fechaTexto.toString().substring(7);
    var mes = fechaTexto.toString().substring(3, 6);
    var dia = fechaTexto.toString().substring(0, 2);
    var fecha = anio + mes + dia;

    fecha = fecha.toString().replace("Ene", "01");
    fecha = fecha.toString().replace("Feb", "02");
    fecha = fecha.toString().replace("Mar", "03");
    fecha = fecha.toString().replace("Abr", "04");
    fecha = fecha.toString().replace("May", "05");
    fecha = fecha.toString().replace("Jun", "06");
    fecha = fecha.toString().replace("Jul", "07");
    fecha = fecha.toString().replace("Ago", "08");
    fecha = fecha.toString().replace("Sep", "09");
    fecha = fecha.toString().replace("Oct", "10");
    fecha = fecha.toString().replace("Nov", "11");
    fecha = fecha.toString().replace("Dic", "12");

    return parseInt(fecha);
}


// Función para obtener la fecha actual del sistema.
function hoy() {
    var today = new Date();
    var monthName = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun',
        'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];

    var year = today.getFullYear();
    var month = today.getMonth();
    var day = today.getDate() < 10 ? '0' + today.getDate() : today.getDate();

    return day + '/' + monthName[month] + '/' + year;
}


// Función que recupera el logotipo de la institución.
function obtenerLogotipo() {
    $.ajax({
        method: "post",
        url: "serviciosc.do",
        data: {name: "GETLOGO"},
        success: function (data, status) {
            var respuestaServ = JSON.parse($.trim(data.toString()));
            
            if(!respuestaServ.error)
                $('#logoEmpresa').atrr('src', "data:image/jpg;base64," + respuestaServ.resource);
        }
    });
}


// Función para actualizar barra de navegación.
function obtenerNavegacion(load) {
    if (load === undefined)
        load = false;
    
    $.ajax({
        method: "post",
        url: "serviciosc.do",
        data: {name: "GETNAVBAR"},
        success: function (data, status) {
            sessionStorage.setItem("navigationBar", data.toString());
            
            if(load){
                $('#userNavBar').html(caracteresEspeciales(data.toString()));
                $(".dropdown-trigger").dropdown();
            }
        }
    });
}


// Función que recupera el mensaje de retroalimentación para el usuario.
function obtenerRetroalimentacion(paginaWeb) {
    // Verifica si existen mensajes de retroalimentacion para el usuario.
    if(!errorSesion)
        $.ajax({
            method: "post",
            url: "serviciosc.do",
            data: {name: "GETMSG",
                   page: paginaWeb.toString()},
            success: function (data, status) {
                var respuestaServ = $.trim(data.toString());

                if (respuestaServ.length > 0)
                    myAlert(respuestaServ);
            }
        });
}


// Función que valida las dos fechas proporcionadas.
function validarFechas(fechaInicial, FechaFinal){
    var intFechaInicio = parseInt(formatearFecha(fechaInicial));
    var intFechaFinal = parseInt(formatearFecha(FechaFinal));
    
    if(intFechaInicio > intFechaFinal)
        return false;
    else
        return true;
}


// Función que valida las dos horas proporcionadas.
function validarHoras(horaInicio, HoraFinal){
    var textoHoraInicio = String(horaInicio);
    var numeroHoraInicio = 0;
    var textoHoraFinal = String(HoraFinal);
    var numeroHoraFinal = 0;
    
    if(textoHoraInicio.length === 0 || textoHoraFinal.length === 0)
        return false;
    
    numeroHoraInicio = 0;
}


// Función que verifica si existe sesión creada.
function verificarSesion(paginaWeb) {
    $.ajax({
        method: "post",
        async: false,
        url: "serviciosc.do",
        data: {name: "GETSSNI",
               page: paginaWeb.toString()},
        success: function (data, status) {
            var respuestaServ = JSON.parse($.trim(data.toString()));
            
            errorSesion = respuestaServ.error;
            if (respuestaServ.error)
                window.location.replace("index.html");
            else if (sessionStorage.getItem("navigationBar") !== null)
                $("#userNavBar").html(caracteresEspeciales(
                                    sessionStorage.getItem("navigationBar")));
            else {
                obtenerNavegacion();
                $("#userNavBar").html(caracteresEspeciales(
                                    sessionStorage.getItem("navigationBar")));
            }
        }
    });
    
    obtenerNavegacion();
    $(".dropdown-trigger").dropdown();
    $('.sidenav').sidenav();
}


// Función que extrae información de la URL.
function getUrlParameter(name) {
    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    var results = regex.exec(location.search);
    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
}


// Función que muestra alerta con caracteres corregidos.
function myAlert(texto) {
    alert(caracteresEspeciales(texto));
}


// Función que muestra mensaje de confirmación con caracteres corregidos.
function myConfirm(texto) {
    return confirm(caracteresEspeciales(texto));
}


// Función que posiciona el cursor en el campo indicado.
jQuery.fn.putCursorAtEnd = function () {
    // Creada a partir de
    // https://css-tricks.com/snippets/jquery/move-cursor-to-end-of-textarea-or-input/
    return this.each(function () {
        // Cache references
        var $el = $(this);
        var el = this;

        // Only focus if input isn't already
        if (!$el.is(":focus"))
            $el.focus();

        // If this function exists... (IE 9+)
        if (el.setSelectionRange) {
            // Double the length because Opera is inconsistent about whether a carriage return is one character or two.
            var len = $el.val().length * 2;

            // Timeout seems to be required for Blink
            setTimeout(function () {
                el.setSelectionRange(len, len);
            }, 1);
        } else {
            // As a fallback, replace the contents with itself
            // Doesn't work in Chrome, but Chrome supports setSelectionRange
            $el.val($el.val());
        }

        // Scroll to the bottom, in case we're in a tall textarea
        // (Necessary for Firefox and Chrome)
        this.scrollTop = 999999;
    });
};


// Función que aplica formato CamelCase al texto proporcionado.
function toCamelCase(str) {
    // Función obtenida a partir de
    // https://www.w3resource.com/javascript-exercises/javascript-string-exercise-11.php

    return str.replace(/\W+(.)/g, function (match, chr) {
        return chr.toUpperCase();
    });
}


// Función que permite reorganizar las tables en base a la columna espeficada.
function sortTable(n, startRow) {
    // Obtenida a partir de 
    // https://www.w3schools.com/howto/howto_js_sort_table.asp
    if (startRow === undefined)
        startRow = 1;

    var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
    table = document.getElementById("myTable2");
    switching = true;
    // Set the sorting direction to ascending:
    dir = "asc";
    /* Make a loop that will continue until
     no switching has been done: */
    while (switching) {
        // Start by saying: no switching is done:
        switching = false;
        rows = table.rows;
        /* Loop through all table rows (except the
         first, which contains table headers): */
        for (i = startRow; i < (rows.length - 1); i++) {
            // Start by saying there should be no switching:
            shouldSwitch = false;
            /* Get the two elements you want to compare,
             one from current row and one from the next: */
            x = rows[i].getElementsByTagName("TD")[n];
            y = rows[i + 1].getElementsByTagName("TD")[n];
            /* Check if the two rows should switch place,
             based on the direction, asc or desc: */
            if (dir == "asc") {
                if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                    // If so, mark as a switch and break the loop:
                    shouldSwitch = true;
                    break;
                }
            } else if (dir == "desc") {
                if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
                    // If so, mark as a switch and break the loop:
                    shouldSwitch = true;
                    break;
                }
            }
        }
        if (shouldSwitch) {
            /* If a switch has been marked, make the switch
             and mark that a switch has been done: */
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
            // Each time a switch is done, increase this count by 1:
            switchcount++;
        } else {
            /* If no switching has been done AND the direction is "asc",
             set the direction to "desc" and run the while loop again. */
            if (switchcount == 0 && dir == "asc") {
                dir = "desc";
                switching = true;
            }
        }
    }
}


// * * * * *   Inicialización de elementos JQuery con MaterializeCSS  * * * * *
$('.datepicker').datepicker({
    format: 'dd/mmm/yyyy',
    i18n: {
        months: ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"],
        monthsShort: ["Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Set", "Oct", "Nov", "Dic"],
        weekdays: ["Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"],
        weekdaysShort: ["Dom", "Lun", "Mar", "Mie", "Jue", "Vie", "Sab"],
        weekdaysAbbrev: ["D", "L", "M", "X", "J", "V", "S"],
        cancel: "Cancelar"
    }
});


$(".dropdown-trigger").dropdown({hover: true});

$('input#input_text, textarea#descripcion').characterCounter();

$('input#input_text, textarea#direccion').characterCounter();

$('.modal').modal();

$('.sidenav').sidenav();

$('select').formSelect();

$('.timepicker').timepicker({
    twelveHour: false,
    i18n: {
        done: "OK",
        cancel: "Cancelar"
    }
});


$('.collapsible').collapsible({accordion: false});
                




























































































// Función que encriopta el texto proporcionado con SHA512.
// Recuperado a partir de
// https://coursesweb.net/javascript/sha512-encrypt-hash_cs
/*
* Secure Hash Algorithm (SHA512)
* http://www.happycode.info/
*/

function SHA512(str) {
 function int64(msint_32, lsint_32) {
 this.highOrder = msint_32;
 this.lowOrder = lsint_32;
 }

 var H = [new int64(0x6a09e667, 0xf3bcc908), new int64(0xbb67ae85, 0x84caa73b),
 new int64(0x3c6ef372, 0xfe94f82b), new int64(0xa54ff53a, 0x5f1d36f1),
 new int64(0x510e527f, 0xade682d1), new int64(0x9b05688c, 0x2b3e6c1f),
 new int64(0x1f83d9ab, 0xfb41bd6b), new int64(0x5be0cd19, 0x137e2179)];

 var K = [new int64(0x428a2f98, 0xd728ae22), new int64(0x71374491, 0x23ef65cd),
 new int64(0xb5c0fbcf, 0xec4d3b2f), new int64(0xe9b5dba5, 0x8189dbbc),
 new int64(0x3956c25b, 0xf348b538), new int64(0x59f111f1, 0xb605d019),
 new int64(0x923f82a4, 0xaf194f9b), new int64(0xab1c5ed5, 0xda6d8118),
 new int64(0xd807aa98, 0xa3030242), new int64(0x12835b01, 0x45706fbe),
 new int64(0x243185be, 0x4ee4b28c), new int64(0x550c7dc3, 0xd5ffb4e2),
 new int64(0x72be5d74, 0xf27b896f), new int64(0x80deb1fe, 0x3b1696b1),
 new int64(0x9bdc06a7, 0x25c71235), new int64(0xc19bf174, 0xcf692694),
 new int64(0xe49b69c1, 0x9ef14ad2), new int64(0xefbe4786, 0x384f25e3),
 new int64(0x0fc19dc6, 0x8b8cd5b5), new int64(0x240ca1cc, 0x77ac9c65),
 new int64(0x2de92c6f, 0x592b0275), new int64(0x4a7484aa, 0x6ea6e483),
 new int64(0x5cb0a9dc, 0xbd41fbd4), new int64(0x76f988da, 0x831153b5),
 new int64(0x983e5152, 0xee66dfab), new int64(0xa831c66d, 0x2db43210),
 new int64(0xb00327c8, 0x98fb213f), new int64(0xbf597fc7, 0xbeef0ee4),
 new int64(0xc6e00bf3, 0x3da88fc2), new int64(0xd5a79147, 0x930aa725),
 new int64(0x06ca6351, 0xe003826f), new int64(0x14292967, 0x0a0e6e70),
 new int64(0x27b70a85, 0x46d22ffc), new int64(0x2e1b2138, 0x5c26c926),
 new int64(0x4d2c6dfc, 0x5ac42aed), new int64(0x53380d13, 0x9d95b3df),
 new int64(0x650a7354, 0x8baf63de), new int64(0x766a0abb, 0x3c77b2a8),
 new int64(0x81c2c92e, 0x47edaee6), new int64(0x92722c85, 0x1482353b),
 new int64(0xa2bfe8a1, 0x4cf10364), new int64(0xa81a664b, 0xbc423001),
 new int64(0xc24b8b70, 0xd0f89791), new int64(0xc76c51a3, 0x0654be30),
 new int64(0xd192e819, 0xd6ef5218), new int64(0xd6990624, 0x5565a910),
 new int64(0xf40e3585, 0x5771202a), new int64(0x106aa070, 0x32bbd1b8),
 new int64(0x19a4c116, 0xb8d2d0c8), new int64(0x1e376c08, 0x5141ab53),
 new int64(0x2748774c, 0xdf8eeb99), new int64(0x34b0bcb5, 0xe19b48a8),
 new int64(0x391c0cb3, 0xc5c95a63), new int64(0x4ed8aa4a, 0xe3418acb),
 new int64(0x5b9cca4f, 0x7763e373), new int64(0x682e6ff3, 0xd6b2b8a3),
 new int64(0x748f82ee, 0x5defb2fc), new int64(0x78a5636f, 0x43172f60),
 new int64(0x84c87814, 0xa1f0ab72), new int64(0x8cc70208, 0x1a6439ec),
 new int64(0x90befffa, 0x23631e28), new int64(0xa4506ceb, 0xde82bde9),
 new int64(0xbef9a3f7, 0xb2c67915), new int64(0xc67178f2, 0xe372532b),
 new int64(0xca273ece, 0xea26619c), new int64(0xd186b8c7, 0x21c0c207),
 new int64(0xeada7dd6, 0xcde0eb1e), new int64(0xf57d4f7f, 0xee6ed178),
 new int64(0x06f067aa, 0x72176fba), new int64(0x0a637dc5, 0xa2c898a6),
 new int64(0x113f9804, 0xbef90dae), new int64(0x1b710b35, 0x131c471b),
 new int64(0x28db77f5, 0x23047d84), new int64(0x32caab7b, 0x40c72493),
 new int64(0x3c9ebe0a, 0x15c9bebc), new int64(0x431d67c4, 0x9c100d4c),
 new int64(0x4cc5d4be, 0xcb3e42b6), new int64(0x597f299c, 0xfc657e2a),
 new int64(0x5fcb6fab, 0x3ad6faec), new int64(0x6c44198c, 0x4a475817)];

 var W = new Array(64);
 var a, b, c, d, e, f, g, h, i, j;
 var T1, T2;
 var charsize = 8;

 function utf8_encode(str) {
 return unescape(encodeURIComponent(str));
 }

 function str2binb(str) {
 var bin = [];
 var mask = (1 << charsize) - 1;
 var len = str.length * charsize;

 for (var i = 0; i < len; i += charsize) {
 bin[i >> 5] |= (str.charCodeAt(i / charsize) & mask) << (32 - charsize - (i % 32));
 }

 return bin;
 }

 function binb2hex(binarray) {
 var hex_tab = '0123456789abcdef';
 var str = '';
 var length = binarray.length * 4;
 var srcByte;

 for (var i = 0; i < length; i += 1) {
 srcByte = binarray[i >> 2] >> ((3 - (i % 4)) * 8);
 str += hex_tab.charAt((srcByte >> 4) & 0xF) + hex_tab.charAt(srcByte & 0xF);
 }

 return str;
 }

 function safe_add_2(x, y) {
 var lsw, msw, lowOrder, highOrder;

 lsw = (x.lowOrder & 0xFFFF) + (y.lowOrder & 0xFFFF);
 msw = (x.lowOrder >>> 16) + (y.lowOrder >>> 16) + (lsw >>> 16);
 lowOrder = ((msw & 0xFFFF) << 16) | (lsw & 0xFFFF);

 lsw = (x.highOrder & 0xFFFF) + (y.highOrder & 0xFFFF) + (msw >>> 16);
 msw = (x.highOrder >>> 16) + (y.highOrder >>> 16) + (lsw >>> 16);
 highOrder = ((msw & 0xFFFF) << 16) | (lsw & 0xFFFF);

 return new int64(highOrder, lowOrder);
 }

 function safe_add_4(a, b, c, d) {
 var lsw, msw, lowOrder, highOrder;

 lsw = (a.lowOrder & 0xFFFF) + (b.lowOrder & 0xFFFF) + (c.lowOrder & 0xFFFF) + (d.lowOrder & 0xFFFF);
 msw = (a.lowOrder >>> 16) + (b.lowOrder >>> 16) + (c.lowOrder >>> 16) + (d.lowOrder >>> 16) + (lsw >>> 16);
 lowOrder = ((msw & 0xFFFF) << 16) | (lsw & 0xFFFF);

 lsw = (a.highOrder & 0xFFFF) + (b.highOrder & 0xFFFF) + (c.highOrder & 0xFFFF) + (d.highOrder & 0xFFFF) + (msw >>> 16);
 msw = (a.highOrder >>> 16) + (b.highOrder >>> 16) + (c.highOrder >>> 16) + (d.highOrder >>> 16) + (lsw >>> 16);
 highOrder = ((msw & 0xFFFF) << 16) | (lsw & 0xFFFF);

 return new int64(highOrder, lowOrder);
 }

 function safe_add_5(a, b, c, d, e) {
 var lsw, msw, lowOrder, highOrder;

 lsw = (a.lowOrder & 0xFFFF) + (b.lowOrder & 0xFFFF) + (c.lowOrder & 0xFFFF) + (d.lowOrder & 0xFFFF) + (e.lowOrder & 0xFFFF);
 msw = (a.lowOrder >>> 16) + (b.lowOrder >>> 16) + (c.lowOrder >>> 16) + (d.lowOrder >>> 16) + (e.lowOrder >>> 16) + (lsw >>> 16);
 lowOrder = ((msw & 0xFFFF) << 16) | (lsw & 0xFFFF);

 lsw = (a.highOrder & 0xFFFF) + (b.highOrder & 0xFFFF) + (c.highOrder & 0xFFFF) + (d.highOrder & 0xFFFF) + (e.highOrder & 0xFFFF) + (msw >>> 16);
 msw = (a.highOrder >>> 16) + (b.highOrder >>> 16) + (c.highOrder >>> 16) + (d.highOrder >>> 16) + (e.highOrder >>> 16) + (lsw >>> 16);
 highOrder = ((msw & 0xFFFF) << 16) | (lsw & 0xFFFF);

 return new int64(highOrder, lowOrder);
 }

 function maj(x, y, z) {
 return new int64(
 (x.highOrder & y.highOrder) ^ (x.highOrder & z.highOrder) ^ (y.highOrder & z.highOrder),
 (x.lowOrder & y.lowOrder) ^ (x.lowOrder & z.lowOrder) ^ (y.lowOrder & z.lowOrder)
 );
 }

 function ch(x, y, z) {
 return new int64(
 (x.highOrder & y.highOrder) ^ (~x.highOrder & z.highOrder),
 (x.lowOrder & y.lowOrder) ^ (~x.lowOrder & z.lowOrder)
 );
 }

 function rotr(x, n) {
 if (n <= 32) {
 return new int64(
 (x.highOrder >>> n) | (x.lowOrder << (32 - n)),
 (x.lowOrder >>> n) | (x.highOrder << (32 - n))
 );
 } else {
 return new int64(
 (x.lowOrder >>> n) | (x.highOrder << (32 - n)),
 (x.highOrder >>> n) | (x.lowOrder << (32 - n))
 );
 }
 }

 function sigma0(x) {
 var rotr28 = rotr(x, 28);
 var rotr34 = rotr(x, 34);
 var rotr39 = rotr(x, 39);

 return new int64(
 rotr28.highOrder ^ rotr34.highOrder ^ rotr39.highOrder,
 rotr28.lowOrder ^ rotr34.lowOrder ^ rotr39.lowOrder
 );
 }

 function sigma1(x) {
 var rotr14 = rotr(x, 14);
 var rotr18 = rotr(x, 18);
 var rotr41 = rotr(x, 41);

 return new int64(
 rotr14.highOrder ^ rotr18.highOrder ^ rotr41.highOrder,
 rotr14.lowOrder ^ rotr18.lowOrder ^ rotr41.lowOrder
 );
 }

 function gamma0(x) {
 var rotr1 = rotr(x, 1), rotr8 = rotr(x, 8), shr7 = shr(x, 7);

 return new int64(
 rotr1.highOrder ^ rotr8.highOrder ^ shr7.highOrder,
 rotr1.lowOrder ^ rotr8.lowOrder ^ shr7.lowOrder
 );
 }

 function gamma1(x) {
 var rotr19 = rotr(x, 19);
 var rotr61 = rotr(x, 61);
 var shr6 = shr(x, 6);

 return new int64(
 rotr19.highOrder ^ rotr61.highOrder ^ shr6.highOrder,
 rotr19.lowOrder ^ rotr61.lowOrder ^ shr6.lowOrder
 );
 }

 function shr(x, n) {
 if (n <= 32) {
 return new int64(
 x.highOrder >>> n,
 x.lowOrder >>> n | (x.highOrder << (32 - n))
 );
 } else {
 return new int64(
 0,
 x.highOrder << (32 - n)
 );
 }
 }

 str = utf8_encode(str);
 strlen = str.length*charsize;
 str = str2binb(str);

 str[strlen >> 5] |= 0x80 << (24 - strlen % 32);
 str[(((strlen + 128) >> 10) << 5) + 31] = strlen;

 for (var i = 0; i < str.length; i += 32) {
 a = H[0];
 b = H[1];
 c = H[2];
 d = H[3];
 e = H[4];
 f = H[5];
 g = H[6];
 h = H[7];

 for (var j = 0; j < 80; j++) {
 if (j < 16) {
 W[j] = new int64(str[j*2 + i], str[j*2 + i + 1]);
 } else {
 W[j] = safe_add_4(gamma1(W[j - 2]), W[j - 7], gamma0(W[j - 15]), W[j - 16]);
 }

 T1 = safe_add_5(h, sigma1(e), ch(e, f, g), K[j], W[j]);
 T2 = safe_add_2(sigma0(a), maj(a, b, c));
 h = g;
 g = f;
 f = e;
 e = safe_add_2(d, T1);
 d = c;
 c = b;
 b = a;
 a = safe_add_2(T1, T2);
 }

 H[0] = safe_add_2(a, H[0]);
 H[1] = safe_add_2(b, H[1]);
 H[2] = safe_add_2(c, H[2]);
 H[3] = safe_add_2(d, H[3]);
 H[4] = safe_add_2(e, H[4]);
 H[5] = safe_add_2(f, H[5]);
 H[6] = safe_add_2(g, H[6]);
 H[7] = safe_add_2(h, H[7]);
 }

 var binarray = [];
 for (var i = 0; i < H.length; i++) {
 binarray.push(H[i].highOrder);
 binarray.push(H[i].lowOrder);
 }
 return binb2hex(binarray);
}