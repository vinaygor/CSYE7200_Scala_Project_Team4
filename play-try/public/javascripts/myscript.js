

var url = "ws://localhost:9000/streamData";

var streamSocket = new WebSocket(url);

var productsOnChart = [], allProductsCount=[];
var ageGenderData = {"0-17":0,"18-25":0,"26-35":0,"36-45":0,"46-50":0,"51-55":0,"55+":0};
var cityCountMap ={"A":Array.apply(null,Array(20)).map(Number.prototype.valueOf,0),"B":Array.apply(null,Array(20)).map(Number.prototype.valueOf,0),"C":Array.apply(null,Array(20)).map(Number.prototype.valueOf,0)};

 var productJsonObject;

var currentUsersCount = [];

var singleProductTicker;

function productCategoryCitygraph()
{
Highcharts.setOptions({
    plotOptions: {
        series: {
            animation: false
        }
    }
});


Highcharts.chart("categoryvscity", {
    chart: {
        type: "bar"
    },
    title: {
        text: "Product Category VS City"
    },
    xAxis: {
        categories: ["1", "2", "3","4","5","6","7","8","9","10","11", "12", "13","14","15","16","17","18","19","20"]
    },
    yAxis: {
        min: 0,
        title: {
            text: "Total products across different Category and City"
        }
    },
    plotOptions : {
                series: {
                    animation : false
                }

                },
    legend: {
        reversed: true
    },
    plotOptions: {
        series: {
            stacking: "normal"
        }
    },
    series: [{
        name: "City A",
        data: cityCountMap["A"]
    }, {
        name: "City B",
        data:  cityCountMap["B"]
    }, {
        name: "City C",
        data:  cityCountMap["C"]
    }]
});

}


function productCategoryCitydataUpdate()
{
    for(var i= 0;i<productJsonObject.length;i++){

    cityCountMap[productJsonObject[i][0][1]][productJsonObject[i][0][0]] = productJsonObject[i][1];
}
productCategoryCitygraph();
}



//all Users Highchart

function refreshChart(){
    Highcharts.chart("container", {
    chart: {
        type: "column"
    },
    title: {
        text: "Top 10 Users Buying Products Chart"
    },
    subtitle: {
        text: "Current Users Buying Count"
    },
        plotOptions : {
        column: {
            animation : false
        }

        },
    xAxis: {
    categories:[],
        type: "category",
        labels: {
            rotation: -45,
            style: {
                fontSize: "13px",
                fontFamily: "Verdana, sans-serif"
            }
        }
    },
    yAxis: {
        min: 0,
        title: {
            text: "Users Buying Products Count"
        }
    },
    legend: {
        enabled: false
    },
    tooltip: {
        pointFormat: "<b>{point.x:.1f} </b> Current Product Buying count: <b>{point.y:.1f} </b>"
    },
    series: [{
        name: "Population",
        data: currentUsersCount,
        dataLabels: {
            enabled: true,
            rotation: -90,
            color: "#FFFFFF",
            align: "right",
            format: "{point.y:.1f}", // one decimal
            y: 10, // 10 pixels down from the top
            style: {
                fontSize: "13px",
                fontFamily: "Verdana, sans-serif"
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

Highcharts.chart("singleProduct", {
    chart: {
        type: "spline",
        animation: Highcharts.svg, // don"t animate in old IE
        marginRight: 10,
        events: {
            load: function () {

                // set up the updating of the chart each second
                var series = this.series[0];

                if(singleProductTicker !== undefined){
                    setInterval(function () {
                        var x = (new Date()).getTime(), // current time
                            y = singleProductTicker[1];
                        series.addPoint([x, y], true, true);
                    }, 1000);
                }
            }
        }
    },
    title: {
        text: "Live Product Sales Ticker :"+ productsOnChart[0][0]
    },
    xAxis: {
        type: "datetime",
        tickPixelInterval: 150
    },
    yAxis: {
        title: {
            text: "Value"
        },
        plotLines: [{
            value: 0,
            width: 1,
            color: "#808080"
        }]
    },
    tooltip: {
        formatter: function () {
            return "<b>" + this.series.name + "</b><br/>" +
                Highcharts.dateFormat("%Y-%m-%d %H:%M:%S", this.x) + "<br/>" +
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
        name: "Currently sold",
        data: (function () {
            // generate an array of random data
            var data = [],
                time = (new Date()).getTime(),
                i;

            for (i = -19; i <= 0; i += 1) {

            if(singleProductTicker !== undefined){
                data.push({
                    x: time + i * 1000,
                    y: singleProductTicker[1]
                });
            }

            }
            return data;
        }())
    }]
});
}

function ageGroupHighChart(){

Highcharts.chart("ageGroup", {
    chart: {
        plotBackgroundColor: null,
        plotBorderWidth: null,
        plotShadow: false,
        type: "pie"
    },
    title: {
        text: "Buying Users count per Age group"
    },
    tooltip: {
        pointFormat: "{series.name}: <b>{point.percentage:.1f}%</b>"
    },
    plotOptions: {
        pie: {
            allowPointSelect: true,
            cursor: "pointer",
            dataLabels: {
                enabled: true,
                format: "<b>{point.name}</b>: {point.percentage:.1f} %",
                style: {
                    color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || "black"
                }
            }
        }
    },
    series: [{
        name: "Number of Buyers",
        colorByPoint: true,
        data: [{
            name: "Age-Group: 0-17",
            y: ageGenderData["0-17"]
        }, {
            name: "Age-Group: 18-25",
            y: ageGenderData["18-25"]
        }, {
            name: "Age-Group: 26-35",
            y: ageGenderData["26-35"]
        }, {
            name: "Age-Group: 36-45",
            y: ageGenderData["36-45"]
        }, {
            name: "Age-Group: 46-50",
            y: ageGenderData["46-50"]
        }, {
            name: "Age-Group: 51-55",
            y: ageGenderData["51-55"]
        }, {
            name: "Age-Group: 55+",
            y: ageGenderData["55+"]
        }]
    }]
});


}



    function availableAnalytics(salesItemRow){

    if(productsOnChart.length !== 0 ){
         singleProductTicker = allProductsCount.find(function(e){return e[0][0] === productsOnChart[0][0]});
    }


       productJsonObject = JSON.parse(salesItemRow);
if(productJsonObject[0][0][1]==="ProductID")
{
   allProductsCount = productJsonObject;
}
else if(productJsonObject[0][0][1]==="AgeGroup"){
    for(var i =0;i<productJsonObject.length;i++){
        ageGenderData[productJsonObject[i][0][0]] = productJsonObject[i][1];
    }
    ageGroupHighChart();
}
else if(productJsonObject[0][0][1]==="UserID")
{
    currentUsersCount=[];
    for(var i =0;i<productJsonObject.length;i++){
   var obj = ["UserID : "+productJsonObject[i][0][0],productJsonObject[i][1]];
    currentUsersCount.push(obj);
}

        refreshChart();
}


else if(productJsonObject!== undefined && productJsonObject[0][0].length==2)
{
    productCategoryCitydataUpdate();

}

    }


streamSocket.onmessage = function (event) {

var data = event.data;

availableAnalytics(data);
};
streamSocket.onopen = function() {
streamSocket.send("streaming");
ageGroupHighChart();
};


    function addProduct() {
    var productId = document.getElementById("product_id").value;
        productsOnChart = [];
        var newProduct = [productId.toString(),1];
        productsOnChart.push(newProduct);
        document.getElementById("product_id").value = "";
        singleproduct();
    }












