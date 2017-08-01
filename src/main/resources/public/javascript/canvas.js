var drawing = false;

$(document).ready(function () {
    document.getElementById("myCanvas").style.cursor = "crosshair";
});

function onClickAlert() {
    var x = event.clientX;
    var y = event.clientY;
    alert("clicked!\n\
           x: " + x + "\n\
           y: " + y);
}

function drawingTrue() {
    drawing = true;
}

function drawingFalse() {
    drawing = false;
}

function test() {
    alert("kosketettu");
}

function testMove() {
    var artikkeli = $("<article/>");
    var teksti1 = $("<p/>");
    teksti1.text("Lorem ipsum... 1");
    artikkeli.append(teksti1);
    $("#body").append(artikkeli);
}

function draw() {
    var canvas = document.getElementById("myCanvas");
    var context = canvas.getContext("2d");
    var x = event.clientX;
    var y = event.clientY;
    var offsetY = getOffset(canvas).top;
    var offsetX = getOffset(canvas).left;

    context.beginPath();
    context.arc(x - offsetX, y - offsetY, 5, 0, 2 * Math.PI, false);
    context.fillStyle = 'green';
    context.fill();
    context.lineWidth = 1;
    context.strokeStyle = '#003300';
    context.stroke();
}

function getOffset(el) {
    el = el.getBoundingClientRect();
    return {
        left: el.left + window.scrollX,
        top: el.top + window.scrollY
    }
}