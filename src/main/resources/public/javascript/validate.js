function forceNumeric(){
    var $input = $(this);
    $input.val($input.val().replace(/[^\d]+/g,''));
}
$('search').on('propertychange input', 'input[type="number"]', forceNumeric);