/*==============SUMMARY================*/
let checkComponentIsClickOrNot = [];

function showPatternDetail(location) {
    let patternId = $("#patternID").val();
    let componentName = $(location).html();
    let index = $(location).parent().index();
    let nextUrl = "/profile-pattern/new-profile-pattern/component-details/" + patternId + "/" + componentName;
    if (!checkComponentIsClickOrNot.includes(componentName)) {
        checkComponentIsClickOrNot.push(componentName);
        $.ajax({
            url: nextUrl,
            type: "get",
            async: false,
            success: function (result) {
                let val = '';
                let detail = '';
                let idDetails = "sub " + componentName;
                result.forEach(result => {
                    val = "<tr id=\"sub\" style='font-size: 90%'>" +
                        "   <td class='pl-4' style='color: #a5a5a5; background-color: #fffff1'>" + result.competencyComponentDetails.componentDetailName + "</td>" +
                        "   <td class=\"text-center\" style='color: #a5a5a5; background-color: #fffff1'>" + result.rankingWeight + "</td>" +
                        "   <td class=\"text-center\" style='color: #a5a5a5; background-color: #fffff1'>" + result.pointDetails + "</td>" +
                        "</tr>";
                    detail = detail + val;
                });
                $('#summaryTable > tbody > tr').eq(index).after(detail);
                $('[id$="sub"]').attr('id', idDetails.replace(/\s+/g, ''));
            }
        });
    } else {
        let idDetails = ("sub " + componentName).replace(/\s+/g, '');
        $('[id$="' + idDetails + '"]').remove();
        checkComponentIsClickOrNot.splice($.inArray(componentName, checkComponentIsClickOrNot), 1);
    }
}

/*==============INDEX PATTERN================*/
let indexPage = 0;

function deletePattern() {
    $(this).one('click', function (event) {
        let conBox = confirm("Are you sure delete this pattern?");
        if (conBox) {
            $.ajax({
                url: $(event.target).parent().attr("href"),
                type: "DELETE",
                async: false,
                success: function () {
                    let tr = $(event.target).closest("tr");
                    tr.fadeIn(1000).fadeOut(200, function () {
                        tr.remove();
                    })
                    searchListPattern(indexPage);
                }
            });
            event.preventDefault();
        }
        event.preventDefault();
    });
}

//=============================DELETE DETAILS======================================
function deletePatternDetails() {
    $(this).one('click', function (event) {
        let conBox = confirm("Are you sure delete this details?");
        console.log($(event.target).parent().attr("href"));
        if (conBox) {
            $.ajax({
                url: $(event.target).parent().attr("href"),
                type: "GET",
                async: false,
                success: function () {
                    location.reload();
                }
            });
            event.preventDefault();
        }
        event.preventDefault();
    });
}

function searchListPattern(index) {
    indexPage = index;
    let page = 0;
    if (index) {
        page = index;
    }
    let nextUrl = "/profile-pattern/list-pattern/" + page;
    $.ajax({
        url: nextUrl,
        type: "get",
        async: false,
        data: {
            title: title.value,
            domainName: domainName.value,
            jobRolesName: jobRolesName.value,
            period: period.value,
            stats: stats.value
        },
        success: function (result) {
            let list = result.content;
            $(".patternList").remove();
            $(".btnIndex").remove();
            let detail = "";
            let timeLoopEmptyTh = 5;
            if (list.length === 0) {
                detail = "<tr class='patternList'>" +
                    "   <td class='text-center' colspan=\"9\" style='height: 31px'>Have No Pattern</td>" +
                    "</tr>";
                timeLoopEmptyTh = 4;
            } else {
                let val = "";
                list.forEach(pattenDTO => {
                    let color = "";
                    if (pattenDTO.activeStatus.toString() === "InProcess") {
                        color = "style='color: gray'";
                    }
                    if (pattenDTO.activeStatus.toString() === "Inactive") {
                        color = "style='color: #e4606d'";
                    }
                    let displayIconCopy;
                    if (pattenDTO.statusPermitCopy === false) {
                        displayIconCopy = "<a class='a-block'\>";
                    } else {
                        displayIconCopy = "<a class='a-block' href=profile-pattern/copyProfilePattern/" + pattenDTO.competencyPatternId + "\>";
                    }
                    val = "<tr class = \"patternList\">\n" +
                        "   <td class=\"pl-2\">" + pattenDTO.jobRoles.domains.domainName + "</td>\n" +
                        "   <td class=\"pl-4\">" + pattenDTO.jobRoles.jobRoleName + "</td>\n" +
                        "   <td class=\"text-center\">" + pattenDTO.periodPattern.name + "-" + pattenDTO.periodPattern.year + "</td>\n" +
                        "   <td class=\"text-center\" title='" + pattenDTO.created + "'>" + (new Date(pattenDTO.created)).toLocaleDateString('en-GB', {
                            year: 'numeric',
                            month: '2-digit',
                            day: '2-digit'
                        }) + "</td>\n" +
                        "   <td class=\"text-center\"" + color + ">" + pattenDTO.activeStatus.toString() + "</td>\n" +
                        "   <td class=\"text-center\">" + (pattenDTO.competencyRankingProfilesList.length > 0 ? 'Yes' : 'No') + "</td>\n" +
                        "   <td class=\"\">\n" +
                        displayIconCopy +
                        "           <img src=\"/icons/copy.png\" title=\"Copy\"  class=\"mini-icon2\" alt=\"Copy\">" +
                        "       </a>\n" +
                        "       <a class='a-block' href=\"/profile-pattern/new-profile-pattern/" + pattenDTO.competencyPatternId + '/' + pattenDTO.competencyComponentsList[0].componentId + '/' + 1 + "\">" +
                        "           <img src=\"/icons/reviewP.png\" title=\"Edit\"  class=\"mini-icon2\" alt=\"Edit\">" +
                        "       </a>\n" +
                        "       <a class='a-block' href=\"/profile-pattern/new-profile-pattern/profile-summary/" + pattenDTO.competencyPatternId + "\">" +
                        "           <img class=\"mini-icon2\" src=\"/icons/summary.png\" title=\"Summary\" class=\"mini-icon2\" alt=\"Summary\">" +
                        "       </a>\n" +
                        "       <a class='a-block' href=\"/profile-pattern/deleteProfilePattern/" + pattenDTO.competencyPatternId + "\">" +
                        "           <img src=\"/icons/delete.png\" onclick=\"deletePattern()\" id=\"deletePattern\" title=\"Delete\"  class=\"mini-icon2\" alt=\"Delete\">" +
                        "       </a>\n" +
                        "   </td>" +
                        "</tr>";
                    detail = detail + val;
                })
            }
            let emptyTr = "";
            for (let i = 0; i < (timeLoopEmptyTh - list.length); i++) {
                emptyTr += "<tr style=\"height: 31px\" class=\"empty-tr patternList\">\n" +
                    "    <td ></td> <td></td> <td></td> <td></td> <td></td> <td></td> <td></td>\n" +
                    "</tr>\n";
            }
            detail += emptyTr;
            $("#body").append(detail);

            let pIndex = "";
            if (index === undefined) {
                index = 0;
            }
            let listPage = [];
            let currentPage = parseInt(index) + 1;
            if (currentPage === 1) {
                listPage.push(currentPage);
                if ((currentPage + 1) <= result.totalPages) {
                    listPage.push(currentPage + 1);
                }
                if ((currentPage + 2) <= result.totalPages) {
                    listPage.push(currentPage + 2);
                }
            } else if (currentPage === result.totalPages) {
                if ((result.totalPages - 2) >= 1) {
                    listPage.push(result.totalPages - 2);
                }
                if ((result.totalPages - 1) >= 1) {
                    listPage.push(result.totalPages - 1);
                }
                listPage.push(result.totalPages);
            } else {
                listPage.push(currentPage - 1);
                listPage.push(currentPage);
                listPage.push(currentPage + 1);
            }

            for (i = 0; i < listPage.length; i++) {
                let colorCurrentPage = "style=\"color: gray; font-size: 90%\"";
                let onclickHTML = "onclick = searchListPattern(" + parseInt(listPage[i] - 1) + ")";
                if (currentPage === listPage[i]) {
                    colorCurrentPage = ""
                    onclickHTML = ""
                }
                pIndex = "<span class='hoverHand btnIndex' id='searchId' " + onclickHTML + " " + colorCurrentPage + "> " + listPage[i] + " </span>";
                $("#pageIndex").append(pIndex);
                $("#pageIndex button").each(function (index, e) {
                    if ($(e).attr('id') === 'searchId') {
                        $(e).attr('id', (i + 1));
                    }
                });
            }
        }
    })
}

/*==============RESET VALUE OF PATTERN INDEX PAGE================*/
function resetValue() {
    $("#title").val('');
    $("#domainName").val('');
    $("#jobRolesName").val('');
    $("#period").val('');
    $("#activeStatus").val('');
    $("#datePattern").val();
    searchListPattern();
}

/*=======================CREATE PATTERN -- SELECT PERIOD/ DOMAIN/ JOB ROLE================*/
function selectPeriodPattern() {
    let periodId = $("#periodCreatePatternPage").val();
    let nextUrl = "/profile-pattern/show-all_domain-name";
    $.ajax({
        url: nextUrl,
        async: false,
        success: function (result) {
            if (periodId == '') {
                $("#domainNameCreatePatternPage").replaceWith("<select name=\"domainName\" id=\"domainNameCreatePatternPage\" class=\"p-1\" disabled >\n" +
                    " <option value=\"\">----</option>\n" +
                    "</select>");
                $("#jobRolesNameCreatePatternPage").replaceWith("<select name=\"jobRolesName\" id=\"jobRolesNameCreatePatternPage\" class=\"p-1\" disabled>\n" +
                    " <option value=\"\">----</option>\n" +
                    "</select>");
                $("#btn-pattern").prop("disabled", true);
                $('input[name="componentCreatePatternPage"]').attr('disabled', 'disabled');
                $('input[name="selectAllComponent"]').attr('disabled', 'disabled');
            } else {
                $('input[name="componentCreatePatternPage"]').removeAttr('disabled');
                $('input[name="selectAllComponent"]').removeAttr('disabled');
                let val = '';
                let data = '';
                result.forEach(domainName => {
                    val = "<option value=\"" + domainName + "\">" + domainName + "</b></option>";
                    data = data + val;
                });
                $("#domainNameCreatePatternPage").replaceWith("<select name=\"domainName\" id=\"domainNameCreatePatternPage\" class=\"p-1\" onchange=\"selectDomain()\">" + data + "</select>");
                selectDomain();
            }
        }
    });

}

function selectDomain() {
    let periodId = $("#periodCreatePatternPage").val();
    let domainName = $("#domainNameCreatePatternPage").val();
    let nextUrl = "/profile-pattern/get-job-roles/" + periodId + "/" + domainName;
    $.ajax({
        url: nextUrl,
        async: false,
        success: function (result) {
            if (periodId == '') {
                $("#jobRolesNameCreatePatternPage").replaceWith("<select name=\"jobRolesName\" id=\"jobRolesNameCreatePatternPage\" class=\"p-1\" disabled>\n" +
                    " <option value=\"\">----</option>\n" +
                    "</select>");
            } else {
                if (result == '') {
                    $("#jobRolesNameCreatePatternPage").replaceWith("<select name=\"jobRolesName\" id=\"jobRolesNameCreatePatternPage\" class=\"p-1\" disabled>\n" +
                        " <option value=\"\">No option</option>\n" +
                        "</select>");
                    $("#btn-pattern").prop("disabled", true);
                    $('input[name="componentCreatePatternPage"]').attr('disabled', 'disabled');
                    $('input[name="selectAllComponent"]').attr('disabled', 'disabled');
                } else {
                    let val = '';
                    let data = '';
                    result.forEach(jobRolesName => {
                        val = "<option value=\"" + jobRolesName + "\">" + jobRolesName + "</b></option>";
                        data = data + val;
                    });
                    $("#jobRolesNameCreatePatternPage").replaceWith("<select name=\"jobRolesName\" id=\"jobRolesNameCreatePatternPage\" class=\"p-1\" onchange='selectJobRoles()'>" + data + "</select>");
                    selectJobRoles();
                    selectComponent();
                }
            }
        }
    });
}

function selectJobRoles() {
    let jobRolesName = $("#jobRolesNameCreatePatternPage").val();
}

/*=======================CREATE PERIOD -- SELECT PERIOD/ YEAR================*/
function selectNamePeriod() {
    let periodName = $("#periodName").val();
    let nextUrl;
    if (periodName != '') {
        nextUrl = "/profile-pattern/select-period-name/" + periodName;
        $.ajax({
            url: nextUrl,
            async: false,
            success: function (result) {
                let val = '';
                let data = '';
                result.forEach(year => {
                    val = "<option value=\"" + year + "\">" + year + "</b></option>";
                    data = data + val;
                });
                $("#year").replaceWith("<select class=\"ml-2 mr-4 p-1\" name=\"year\" id=\"year\" style=\"width: 20%\" th:field=\"*{year}\">" + data + "</select>");
                $("#createPeriod").prop("disabled", false);

            }
        })
    } else {
        $("#year").replaceWith("<select class=\"ml-2 mr-4 p-1\" name=\"year\" id=\"year\" style=\"width: 20%\" th:field=\"*{year}\" disabled>\n" +
            "<option value=\"\">----</option>\n" +
            "</select>");
        $("#createPeriod").prop("disabled", true);
    }


}

/*=======================SELECT COMPONENTS================*/
function selectComponent() {
    $("#checkbox-container input").change(function () {
        if ($('input[name="componentCreatePatternPage"]:checked').length > 0) {
            if ($("#periodCreatePatternPage").val() != '') {
                $("#btn-pattern").prop("disabled", false);
            } else {
                $("#btn-pattern").prop("disabled", true);
            }
        } else {
            $("#btn-pattern").prop("disabled", true);
        }
    })
}

/*=======================SELECT All COMPONENTS================*/
function selectAllComponent() {
    if ($("#selectAllComponent > input:checked").val() != null) {
        if ($("#periodCreatePatternPage").val() != '') {
            $("#btn-pattern").prop("disabled", false);
            $('input[name="componentCreatePatternPage"]').prop('checked', true);
        } else {
            $('input[name="componentCreatePatternPage"]').prop('checked', false);
            $("#btn-pattern").prop("disabled", true);
        }
    } else {
        $('input[name="componentCreatePatternPage"]').prop('checked', false);
        $("#btn-pattern").prop("disabled", true);
    }
}


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


