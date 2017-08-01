var drawing = false;

$(document).ready(function () {
    var canvas = document.getElementById("myCanvas");
    canvas.style.cursor = "crosshair";
    if (is_touch_device()) {
        canvas.addEventListener("touchstart", touchHandler, false);
    }
});

function is_touch_device() {
    return 'ontouchstart' in window;
}

function touchHandler(event) {
    drawingTrue();
    draw();
}

function drawingTrue() {
    drawing = true;
}

function drawingFalse() {
    drawing = false;
}

function test() {
    var x = event.touches[0].pageX;
    var y = event.touches[0].pageY;
    alert("x: " + x + "\n\
           y: " + y);
}

function draw() {
    var x = 0;
    var y = 0;
    if (is_touch_device()) {
        x = event.touches[0].pageX;
        y = event.touches[0].pageY;
    } else {
        x = event.clientX;
        y = event.clientY;
    }
    if (drawing) {
        var canvas = document.getElementById("myCanvas");
        var context = canvas.getContext("2d");
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

}

function getOffset(el) {
    el = el.getBoundingClientRect();
    return {
        left: el.left + window.scrollX,
        top: el.top + window.scrollY
    }
}