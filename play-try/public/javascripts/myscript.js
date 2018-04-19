
console.log("I am here")
var url = "ws://localhost:9000/streamData";

var streamSocket = new WebSocket(url);
streamSocket.onmessage = function (event) {

console.log(event);
//debugger;
var data = event.data;

availableAnalytics(data);
};
streamSocket.onopen = function() {
streamSocket.send("streaming");
};



// main logic
var productsOnChart = [];
var currentProductsCount = [["1002181",0]];

    function availableAnalytics(product){
        console.log(product);
       var found = currentProductsCount.findIndex(function(element) {
            return element[0] == product;
        });
        debugger;
        //Product is not present
        if(found == -1){
            var product = [product,1];
            currentProductsCount.push(product);
        }
        else{
            currentProductsCount[found][1] = currentProductsCount[found][1] + 1;
        }
        refreshChart();
        singleproduct();
    }

    function addProduct() {
    var productId = document.getElementById('product_id').value;
        console.log("Adding Product: "+productId);
        var newProduct = [productId.toString(),1];
        productsOnChart.push(newProduct);

    }




//allProducts Highchart

function refreshChart(){
    Highcharts.chart('container', {
    chart: {
        type: 'column'
    },
    title: {
        text: 'Real-time Product Sales Chart'
    },
    subtitle: {
        text: 'Current Products Sales Data'
    },
        plotOptions : {
        column: {
            animation : false
        }

        },
    xAxis: {
        type: 'category',
        labels: {
            rotation: -45,
            style: {
                fontSize: '13px',
                fontFamily: 'Verdana, sans-serif'
            }
        }
    },
    yAxis: {
        min: 0,
        title: {
            text: 'Product Sales Count'
        }
    },
    legend: {
        enabled: false
    },
    tooltip: {
        pointFormat: 'Current Product Sales count: <b>{point.y:.1f} </b>'
    },
    series: [{
        name: 'Population',
        data: currentProductsCount,
        dataLabels: {
            enabled: true,
            rotation: -90,
            color: '#FFFFFF',
            align: 'right',
            format: '{point.y:.1f}', // one decimal
            y: 10, // 10 pixels down from the top
            style: {
                fontSize: '13px',
                fontFamily: 'Verdana, sans-serif'
            }
        }
    }]
});
}

//singleProduct Analysis

function singleproduct(){
Highcharts.setOptions({
    global: {
        useUTC: false
    }
});

Highcharts.chart('singleProduct', {
    chart: {
        type: 'spline',
        animation: Highcharts.svg, // don't animate in old IE
        marginRight: 10,
        events: {
            load: function () {

                // set up the updating of the chart each second
                var series = this.series[0];
                setInterval(function () {
                    var x = (new Date()).getTime(), // current time
                        y = Math.random();
                    series.addPoint([x, y], true, true);
                }, 1000);
            }
        }
    },
    title: {
        text: 'Live random data'
    },
    xAxis: {
        type: 'datetime',
        tickPixelInterval: 150
    },
    yAxis: {
        title: {
            text: 'Value'
        },
        plotLines: [{
            value: 0,
            width: 1,
            color: '#808080'
        }]
    },
    tooltip: {
        formatter: function () {
            return '<b>' + this.series.name + '</b><br/>' +
                Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
                Highcharts.numberFormat(this.y, 2);
        }
    },
    legend: {
        enabled: false
    },
    exporting: {
        enabled: false
    },
    series: [{
        name: 'Random data',
        data: (function () {
            // generate an array of random data
            var data = [],
                time = (new Date()).getTime(),
                i;

            for (i = -19; i <= 0; i += 1) {
                data.push({
                    x: time + i * 1000,
                    y: Math.random()
                });
            }
            return data;
        }())
    }]
});
}

