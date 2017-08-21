function forceNumeric(){
    var $input = $(this);
    $input.val($input.val().replace(/[^\d]+/g,''));
}
$('body').on('propertychange input', 'input[type="number"]', forceNumeric);