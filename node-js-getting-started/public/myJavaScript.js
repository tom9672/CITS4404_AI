const WEATHER_API_URL = 'http://api.openweathermap.org/data/2.5/weather?q=Perth&appid=fb6dc782bd6680ccdd922c06a38e2d80&units=metric';


var lastTimeTakeWater_time="";
var lastTimeTakeWater_distance=0;
async function getData(){
    const response = await fetch(WEATHER_API_URL);
    const data = await response.json();
    //console.log(data);
    const city = data.name;
    const temp = data.main.temp;
    const weather = data.weather[0].description;
    
    document.getElementById("city").innerHTML = city +"\t | \t"+ temp +"℃" + "\t | \t"+ weather + "<br>";
    document.getElementById("remind").innerHTML = "You drinked "+lastTimeTakeWater_distance/lastTimeTakeWater_time+ " in the previous " + lastTimeTakeWater_time+" hour(s)";

    
    

}


function readTextFile(file, callback) {
  var rawFile = new XMLHttpRequest();
  rawFile.overrideMimeType("application/json");
  rawFile.open("GET", file, true);
  rawFile.onreadystatechange = function() {
      if (rawFile.readyState === 4 && rawFile.status == "200") {
          callback(rawFile.responseText);
      }
  }
  rawFile.send(null);
}

var time =[];
var distance =[];
var time_distance = [];
readTextFile("../data.json", function(text){
  var data = JSON.parse(text);
  var waterTakeData = data.waterTakeData;
  for(var i=0; i<waterTakeData.length; i++){
    var time_distance_each = [];
    time.push(waterTakeData[i].time);
    distance.push(waterTakeData[i].distance);

    time_distance_each.push(waterTakeData[i].time);
    time_distance_each.push(parseInt(waterTakeData[i].distance));
    time_distance.push(time_distance_each);
  } 
  console.log(time);
  console.log(distance);
  console.log(time_distance);
  lastTimeTakeWater_time = (Date.parse(time[time.length-1]) - Date.parse(time[time.length-2]))/(3600*1000);
  lastTimeTakeWater_distance = distance[distance.length-1] - distance[distance.length-2];

    // var ctx = document.getElementById('myChart').getContext('2d');
    // var myChart = new Chart(ctx, {
    //     type: 'bar',
    //     data: {
    //         labels: time,
    //         datasets: [{
    //             label: '# of distance',
    //             data: distance,
                
    //             borderWidth: 1
    //         }]
    //     },
    //     options: {
    //         scales: {
    //             yAxes: [{
    //                 ticks: {
    //                     beginAtZero: true
    //                 }
    //             }]
    //         }
    //     }
    // });





    
      
      // function addData() {
        
      //     var newwidth = $('.chartAreaWrapper2').width() + 200;
      //     $('.chartAreaWrapper2').width(newwidth);
        
      // }
      
      // var chartData = {
      //   labels: time,
      //   datasets: [{
      //     label: "Test Data Set",
      //     data: distance
      //   }]
      // };
      
      // $(function() {
      //   var canvasFuelSpend = $('#chart-FuelSpend');
      //   var chartFuelSpend = new Chart(canvasFuelSpend, {
      //     type: 'bar',
      //     data: chartData,
      //     maintainAspectRatio: false,
      //     responsive: true,
      //     options: {
      //       tooltips: {
      //         titleFontSize: 0,
      //         titleMarginBottom: 0,
      //         bodyFontSize: 12
      //       },
      //       legend: {
      //         display: false
      //       },
      //       scales: {
      //         xAxes: [{
      //           ticks: {
      //             fontSize: 12,
      //             display: false
      //           }
      //         }],
      //         yAxes: [{
      //           ticks: {
      //             fontSize: 12,
      //             beginAtZero: true
      //           }
      //         }]
      //       },
      //       animation: {
      //         onComplete: function() {
      //           var sourceCanvas = chartFuelSpend.chart.canvas;
      //           var targetCtx = document.getElementById("axis-FuelSpend").getContext("2d");
      //           targetCtx.drawImage(sourceCanvas,0,0);
      //         }
      //       }
      //     }
      //   });
      //   addData();
      // });





//       Highcharts.chart('container', {
//         chart: {
//             type: 'bar',
//             marginLeft: 100,
//             inverted: true
//         },
//         title: {
//             text: 'Water Intake Distribution'
//         },
//         subtitle: {
//             text: 'Remind you to drink more water'
//         },
//         xAxis: {
//             type: 'category',
//             title: {
//                 text: null
//             },
//             min: 0,
//             max: 4,
//             scrollbar: {
//                 enabled: true
//             },
//             tickLength: 0
//         },
//         yAxis: {
//             min: 0,
//             max: 20,
//             title: {
//                 text: 'Distance',
//                 align: 'high'
//             }
//         },
//         plotOptions: {
//             bar: {
//                 dataLabels: {
//                     enabled: false
//                 }
//             }
//         },
//         legend: {
//             enabled: false
//         },
//         credits: {
//             enabled: false
//         },
//         series: [{
//             name: 'Distance',
//             data: time_distance
//         }]
//       });




var chart = Highcharts.chart('container', {
    chart: {
        type: 'column'
    },
    title: {
        text: 'Water intake distribution'
    },
    subtitle: {
        text: 'Remind you to drink water'
    },
    xAxis: {
        type: 'category',
        min: 0,
        max: 8,
        scrollbar: {
            enabled: true
        },
        labels: {
            rotation: -45 
        }
       
    },
    yAxis: {
        min: 0,
        title: {
            text: 'Water distance'
        }
    },
    legend: {
        enabled: false
    },
    tooltip: {
        pointFormat: 'Distance: <b>{point.y:.1f} CM</b>'
    },
    series: [{
        name: '',
        data: time_distance,
        dataLabels: {
            enabled: true,
            rotation: -90,
            color: '#FFFFFF',
            align: 'right',
            format: '{point.y:.1f}', 
            y: 10
        }
    }]
});

});







  

getData()

