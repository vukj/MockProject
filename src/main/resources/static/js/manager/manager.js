/*=======================MANAGER RADAR CHART================*/
function patternRadarChart() {
    let patternId = $("#patternID").val();
    let nextUrl = "/profile-pattern/new-profile-pattern/json-pattern-summary/" + patternId;
    $(document).ready(function () {
        $.ajax({
            url: nextUrl,
            async: false,
            success: function (patternSummaryDTOList) {
                let componentNameList = [];
                let rankingWeightList = [];
                let basePointList = [];

                $.each(patternSummaryDTOList, function (index, value) {
                    componentNameList.push(value.competencyComponents.componentName);
                    rankingWeightList.push(100);
                    basePointList.push(value.basePoint * 100 / value.totalWeight);
                });

                let marksCanvas = document.getElementById("canvas");
                let options = {
                    scales: {
                        r: {
                            angleLines: {
                                display: true
                            },
                            suggestedMin: 0,
                        }
                    }
                };
                let marksData = {
                    labels: componentNameList,
                    datasets: [
                        {
                            label: "Ranking Weight",
                            pointStyle: 'circle',
                            pointBorderWidth: 2,
                            pointBackgroundColor: "rgba(200,0,0,1)",
                            borderDash: [3, 5],
                            borderWidth: 2,
                            borderColor: "rgba(200,0,0,0.5)",
                            backgroundColor: "rgba(200,0,0,0.03)",
                            data: rankingWeightList,
                        },
                        {
                            label: "Require Point",
                            pointStyle: 'circle',
                            pointBorderWidth: 2,
                            pointBackgroundColor: "rgba(0,0,200,1)",
                            borderDash: [3, 5],
                            borderWidth: 2,
                            borderColor: "rgba(0,0,200,0.5)",
                            backgroundColor: "rgba(0,0,200,0.03)",
                            data: basePointList,
                        }
                    ]
                };
                let radarChart = new Chart(marksCanvas, {type: 'radar', data: marksData, options: options});
            },
        });
    });
}
