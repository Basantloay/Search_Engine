import $ from 'jquery';
$(document).ready(function() {
    var list_length=0.5*($('.list-group').outerHeight());
    var input_length=($('.input-group').outerHeight());
    console.log(list_length);
    console.log(input_length);
    $('.list-group').css({
        top:0.5*($('.searchAndResults').outerHeight())+input_length+5+'px'
    });
});