// Search list Profile to show in my-profile/index
function searchList(index) {
    $(document).ready(function () {
        let page = 0;
        if (index) {
            page = index;
        }
        let nextUrl = "/my-profiles/list-profile/" + page;
        $.ajax({
            url: nextUrl,
            type: "get",
            async: false,
            data: {
                title: title.value,
                period: period.value,
                createdDate: createdDate.value,
                status: stats.value,
                roles: roles.value
            },
            success: function (result) {
                let list = result.content;
                $(".profilesList").remove();
                $("#pageIndex").empty();
                let detail = "";
                let timeLoopEmptyTh = 5;
                if (list.length === 0) {
                    detail = "<tr class='profilesList'>" +
                        "   <td class='text-center' colspan='6' style='height: 31px'>Have No Profile</td>" +
                        "</tr>";
                    timeLoopEmptyTh = 4;
                } else {
                    let val = "";
                    list.forEach(profileDTO => {
                        let actionReview = "";
                        if ((profileDTO.statusTypes === "Completed") || (profileDTO.statusTypes === "Submitted") || (profileDTO.statusTypes === "InProcess")) {
                            actionReview = "href='/new-profile/edit/" + profileDTO.rankingProfileId + "'";
                        }
                        let colorStatus = "";
                        if ((profileDTO.statusTypes === "Rejected") || (profileDTO.statusTypes === "ReviewedFail")) {
                            colorStatus = "style=\"color: #e4606d\"";
                        }
                        if ((profileDTO.statusTypes === "Reviewing") || (profileDTO.statusTypes === "Reviewed")) {
                            colorStatus = "style=\"color: #419def\"";
                        }
                        if (profileDTO.statusTypes === "Approved") {
                            colorStatus = "style=\"color: 'blue'\"";
                        }
                        if ((profileDTO.statusTypes === "InProcess") || (profileDTO.statusTypes === "Completed")) {
                            colorStatus = "style=\"color: #a0a0a0\"";
                        }
                        val = "                       <tr id='profile" + profileDTO.rankingProfileId + "' class='profilesList'>\n" +
                            "                            <td class='pl-3'>" + profileDTO.profileTitle + "<br class=\"horizonal-menu\"></td>\n" +
                            "                            <td class='text-center'>" + profileDTO.competencyRankingPatterns.periodPattern.name + "-" + profileDTO.competencyRankingPatterns.periodPattern.year + "</td>\n" +
                            "                            <td class='text-center' title='" + profileDTO.created + "'>" + (new Date(profileDTO.created)).toLocaleDateString('en-GB', {
                                year: 'numeric',
                                month: '2-digit',
                                day: '2-digit'
                            }) + "</td>\n" +
                            "                            <td class='pl-3' " + colorStatus + ">" + profileDTO.statusTypes + "</td>\n" +
                            "                            <td>" + profileDTO.competencyRankingPatterns.jobRoles.jobRoleName + " " + profileDTO.competencyRankingPatterns.jobRoles.domains.domainName + "\n" +
                            "                            </td>\n" +
                            "                            <td class='text-center'>\n" +
                            "                                <a class='a-block' " + actionReview + " >" +
                            "                                   <img class='mini-icon2' src='/icons/reviewP.png' title='Edit'>" +
                            "                                </a>\n" +
                            "                                <a class='a-block' href='/new-profile/detail/" + profileDTO.rankingProfileId + "'>\n" +
                            "                                    <img class='mini-icon2' src='/icons/summary.png' title='Summary'>" +
                            "                                </a>\n" +
                            "                                <a class='a-block' href='/my-profiles/copy/" + profileDTO.rankingProfileId + "'>" +
                            "                                    <img class='mini-icon2' src='/icons/copy.png' title='Copy'>" +
                            "                                </a>\n" +
                            "                                <img onclick='deleteProfile(" + profileDTO.rankingProfileId + ")' class='mini-icon2' src='/icons/delete.png' title='Delete'>\n" +
                            "                            </td>\n" +
                            "                        </tr>\n";
                        detail = detail + val;
                    })
                }
                let emptyTr = "";
                for (let i = 0; i < (timeLoopEmptyTh - list.length); i++) {
                    emptyTr += "<tr style=\"height: 31px\" class=\"empty-tr profilesList\">\n" +
                        "    <td ></td> <td></td> <td></td> <td></td> <td></td> <td></td>\n" +
                        "</tr>\n";
                }
                detail += emptyTr;
                $("#body").append(detail);
                $("#pageContainer").empty();
                $("#pageContainer").append("<input id='page' type='hidden' name='page' value='" + page + "'>\n")
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
                    let onclickHTML = "onclick = searchList(" + parseInt(listPage[i] - 1) + ")";
                    if (currentPage === listPage[i]) {
                        colorCurrentPage = ""
                        onclickHTML = ""
                    }
                    pIndex += "<span class='hoverHand btnIndex' id='searchId' " + onclickHTML + " " + colorCurrentPage + "> " + listPage[i] + " </span>\n";
                }

                if (list.length === 0) {
                    $("#pageIndex").empty();
                } else {
                    $("#pageIndex").append(pIndex);
                }
            }
        })
    })
}

// Check Profile exist and save
function checkProfile() {
    if (saveConfirmDialog() == false) {
        return;
    } else {
        let nextUrl = "/my-profiles/check";
        let title = $("#title").val();
        let patternId = $("#patternId").val();
        let rankingProfileId = $("#rankingProfileId").val();
        $.ajax({
            url: nextUrl,
            type: "post",
            async: false,
            data: {title: title, patternId: patternId, rankingProfileId: rankingProfileId},
            success: function (result) {
                if (result) {
                    window.alert("Title for this Pattern already exits!");
                    return;
                } else {
                    $('#submitForm').submit();
                }
            }
        })
    }
}

// Get List Detail to show in new-profile/index
function getListDetail(page) {
    $(document).ready(function () {
        let nextUrl = "/new-profile/detail-json/";
        $.ajax({
            url: nextUrl,
            type: "get",
            async: false,
            data: {page: page},
            success: function (wrapperList) {
                let list = wrapperList.competencyRankingProfileDetailsList;
                let listDetails = list[0].competencyRankingProfiles.competencyRankingProfileDetailsList;
                $("#form").empty();
                $("#dot").empty();
                $("#btnDetails").empty();
                $("#point").empty();
                // $("#profile").empty();
                let val = "";
                let result = "";
                let source = "";
                let sourceList = "";
                let lvl = "";
                let lvlList = "";
                let component = "";
                let componentDot = "";
                let btnSave = "";
                let btnContinue = "";
                let btnCancel = "";
                // Dot List
                list[0].competencyRankingPatternDetails.competencyRankingPatterns.competencyComponentsList.forEach((competencyComponent, index) => {
                    let dot = "🔘";
                    for (let i = 0; i < listDetails.length; i++) {
                        if (listDetails[i].competencyRankingPatternDetails.competencyComponentDetails.competencyComponents.componentId == competencyComponent.componentId && listDetails[i].proficiencyLevels == null) {
                            dot = "⭕";
                            break;
                        } else {
                            dot = "🔘";
                        }
                    }
                    if (page == index) {
                        dot = "🔴";
                    }
                    component = "<span>---<button style='border:0; padding:0; outline:0; background:transparent;'\n" +
                        "                                       onclick='getListDetail(" + index + ")' title='" + competencyComponent.componentName + "' class='dot-non-hover'><span>" + dot + "</span></button></span>";

                    componentDot = componentDot + component;
                })
                $("#dot").append("" + componentDot + "<span>---<button style='border:0; padding:0; outline:0; background:transparent;'\n" +
                    "                                   onclick='summary(" + list[0].competencyRankingProfiles.rankingProfileId + ")' title='Profile Ranking Summary' class='dot-non-hover'><span>🔘</span></button>---</span>");
                $("#point").append("Self-Assessment Points: <b>" + list[0].competencyRankingProfiles.selfRakingPoints + "</b>");
                // List DataSource
                list.forEach(profileDetails => {
                    let selectHTML = "";
                    sourceList = "";
                    if (profileDetails.competencyRankingPatternDetails.evidenceTypes.toString() === 'Optional') {
                        source = "<option value = ''> UnRequired </option>";
                        sourceList = source;
                    }
                    if (profileDetails.competencyRankingPatternDetails.evidenceTypes.toString() === 'Required') {
                        sourceList = "";
                        profileDetails.competencyRankingPatternDetails.competencyComponentDetails.dataSourcesList.forEach(dataSources => {
                            if (profileDetails.dataSources != null && dataSources.dataSourceId == profileDetails.dataSources.dataSourceId) {
                                source = "<option selected value='" + dataSources.dataSourceId + "'>" + dataSources.dataSourceName + "</option>";
                            } else {
                                source = "<option value='" + dataSources.dataSourceId + "'>" + dataSources.dataSourceName + "</option>";
                            }
                            sourceList = sourceList + source;
                        })
                        selectHTML = "<select class='dataSources text-center' field='" + profileDetails.dataSources + "'>" + sourceList + "</select>\n";
                    }
                    // List Levels
                    lvlList = "";
                    profileDetails.competencyRankingPatternDetails.competencyComponentDetails.proficiencyLevelTypes.proficiencyLevelsList.forEach(levels => {
                        if (profileDetails.proficiencyLevels != null && levels.proficiencyLevelId == profileDetails.proficiencyLevels.proficiencyLevelId) {
                            lvl = "<option selected title='None: None performs tasks' value='" + levels.proficiencyLevelId + "'>" + levels.proficiencyLevelName + "</option>";
                        } else {
                            lvl = "<option title='None: None performs tasks' value='" + levels.proficiencyLevelId + "'>" + levels.proficiencyLevelName + "</option>";
                        }
                        lvlList = lvlList + lvl;
                    });
                    // Details List
                    let detail = "";
                    if (profileDetails.proficiencyLevels == null) {
                        detail = "<span style='color: #9d9d9d'>" + profileDetails.competencyRankingPatternDetails.competencyComponentDetails.componentDetailName + "</span>";
                    } else {
                        detail = "<span>" + profileDetails.competencyRankingPatternDetails.competencyComponentDetails.componentDetailName + "</span>";
                    }

                    val = " <tr class='detailList'>\n" +
                        "   <td field='" + profileDetails.competencyRankingPatternDetails + "' class='text-left' tr-click='table-row-0' value='" + profileDetails.competencyRankingPatternDetails.competencyPatternDetailId + "'>\n" +
                        "   " + detail + "</td>\n" +
                        "   <!--Source info-->\n" +
                        "   <td class='text-center'>\n" +
                        selectHTML +
                        "   </td>\n" +
                        "   <!--Ranking Weight-->\n" +
                        "   <td class='text-center' name='rankingWeight' value='" + profileDetails.competencyRankingPatternDetails.rankingWeight + "'\">" + profileDetails.competencyRankingPatternDetails.rankingWeight + "</td>\n" +
                        "   <!--Ranking Level-->\n" +
                        "   <td style='vertical-align:baseline;'>\n" +
                        "   <select class='proficiencyLevels' field='" + profileDetails.proficiencyLevels + "' style='width:150px;'>" + lvlList + "</select>\n" +
                        "   </td>\n" +
                        " </tr>";
                    result = result + val;
                })
                // Table Details
                btnSave = "<button class='btn btn-primary mr-3' type='submit' onclick='saveDetail(" + list[0].competencyRankingProfiles.rankingProfileId + "," + page + ")' role='button'>Save &amp; Finish</button>";
                if ((page + 1) < list[0].competencyRankingPatternDetails.competencyRankingPatterns.competencyComponentsList.length) {
                    btnContinue = "<button class='btn btn-primary mr-3' type='submit' onclick='saveDetail(" + null + "," + page + ")' role='button' >Save &amp; Continue</button>";
                }
                btnCancel = "<a class='btn btn-primary' href='/my-profiles' role='button' >Return</a>";
                $("#form").append("<table id='table' border='0' style='width:100%;' class='col-12 mb-2 smaller-font table-profile table-striped-custom'>\n" +
                    "                                <thead>\n" +
                    "                                    <!--Component-->\n" +
                    "                                    <tr field='" + list[0].competencyRankingPatternDetails.competencyComponentDetails.competencyComponents + "'>\n" +
                    "                                        <th style='width: 40%' class='' value='" + list[0].competencyRankingPatternDetails.competencyComponentDetails.competencyComponents.componentId + "'>" +
                    list[0].competencyRankingPatternDetails.competencyComponentDetails.competencyComponents.componentName + "" +
                    "                                        </th>\n" +
                    "                                        <th style='width: 20%' class='text-center'>Source Information</th>\n" +
                    "                                        <th style='width: 10%' class='text-center'>R.Weight(%)</th>\n" +
                    "                                        <th style='width: 30%' class='text-center'>Responsibility Level</th>\n" +
                    "                                    </tr>\n" +
                    "                                </thead>\n" +
                    "                                <tbody>\n" +
                    "                                    <!--Component Details-->\n" +
                    "                                   " + result + "\n" +
                    "                                </tbody>\n" +
                    "                                <input type='hidden' name='page' value='" + page + "'>\n" +
                    "                            </table>\n" +
                    "                            <div><i>You are now can update and finish profile OR update profile and continue to rankings of Component page.</i></div>");
                $("#btnDetails").append(btnSave + btnContinue + btnCancel);
            }
        })
    })
}

// Save Detail Profile
function saveDetail(id, page) {
    let nextUrl = "/new-profile/save/" + page;
    let detailsList = $(".detailList");
    let dataSources = $(".dataSources");
    let proficiencyLevels = $(".proficiencyLevels");
    let profileDetailWrapperList = {};
    let competencyRankingProfileDetailsList = [];
    for (let i = 0; i < detailsList.length; i++) {
        let dataSourcesId = "";
        if (dataSources[i] != undefined) {
            dataSourcesId = dataSources[i].value;
        }
        let proficiencyLevelsId = proficiencyLevels[i].value;
        itemData = {};
        itemData ["dataSourceId"] = dataSourcesId;
        itemLevels = {};
        itemLevels ["proficiencyLevelId"] = proficiencyLevelsId;
        item = {};
        item ["dataSources"] = itemData;
        item ["proficiencyLevels"] = itemLevels;
        competencyRankingProfileDetailsList.push(item);
    }
    profileDetailWrapperList ["competencyRankingProfileDetailsList"] = competencyRankingProfileDetailsList;
    if (id != null) {
        $.ajax({
            url: nextUrl,
            type: "post",
            async: false,
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify(profileDetailWrapperList),
            success: function () {
            }
        });
        summary(id);
    } else {
        $.ajax({
            url: nextUrl,
            type: "post",
            async: false,
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify(profileDetailWrapperList),
            success: function () {
            }
        });
        getListDetail(page + 1);
    }
}

// Get data profile to show in new-profile/summary
function summary(id) {
    $(document).ready(function () {
        let nextUrl = "/new-profile/json-summary";
        $.ajax({
            url: nextUrl,
            type: "get",
            async: false,
            success: function (profileSummaryDTOList) {
                let radar = "";
                let radarList = "";
                let component = "";
                let componentDot = "";
                let btnSubmit = "";
                $("#form").empty();
                $("#dot").empty();
                $("#btnDetails").empty();
                $("#point").empty();
                $("#point").append("Self-Assessment Points: <b>" + profileSummaryDTOList[0].competencyRankingProfiles.selfRakingPoints + "</b>");
                // Dot List
                profileSummaryDTOList.forEach((profile, index) => {
                    if (profile.status == 0) {
                        component = "<span>---<button style='border:0; padding:0; outline:0; background:transparent;'\n" +
                            "                                       onclick='getListDetail(" + index + ")' title='" + profile.competencyComponents.componentName + "' class='dot-non-hover'><span >⭕</span></button></span>";
                    } else {
                        component = "<span>---<button style='border:0; padding:0; outline:0; background:transparent;'\n" +
                            "                                       onclick='getListDetail(" + index + ")' title='" + profile.competencyComponents.componentName + "' class='dot-non-hover'><span >🔘</span></button></span>";
                    }
                    componentDot = componentDot + component;
                });
                $("#dot").append("" + componentDot + "<span>---<button style='border:0; padding:0; outline:0; background:transparent;'\n" +
                    "                                   onclick='summary()' title='Profile Ranking Summary' class='dot-non-hover'><span>🔴</span>---</button></span>");
                // Profile Table
                let style = "";
                let patternWeight = 0;
                let selfWeight = 0;
                let reviewWeight = 0;
                profileSummaryDTOList.forEach(profileSummary => {
                    if (profileSummary.status == 0) {
                        style = "style='color:#e4606d'";
                    } else {
                        style = "style=''";
                    }
                    radar = "<tr " + style + "><td class='pl-3'>" + profileSummary.competencyComponents.componentName + "</td>\n" +
                        "                        <td class='text-center'>" + profileSummary.patternRankWeight + "</td>\n" +
                        "                        <td class='text-center'>" + profileSummary.selfRankWeight + "</td>\n" +
                        "                        <td class='text-center'>" + profileSummary.reviewRankWeight + "</td></tr>";
                    radarList += radar;
                    selfWeight += parseFloat(profileSummary.selfRankWeight);
                    patternWeight += parseFloat(profileSummary.patternRankWeight);
                    reviewWeight += parseFloat(profileSummary.reviewRankWeight);
                })
                // Button
                if (profileSummaryDTOList[0].competencyRankingProfiles.statusTypes.toString() === 'Completed') {
                    btnSubmit = "<button class='btn btn-primary ml-2' onclick='actionSubmitProfile(" + id + ", \"Submit\")'>Submit Profile</button>";
                }
                if (profileSummaryDTOList[0].competencyRankingProfiles.statusTypes.toString() === 'Submitted') {
                    btnSubmit = "<button class='btn btn-primary ml-2' onclick='actionSubmitProfile(" + id + ", \"UnSubmit\")'>UnSubmit Profile</button>";
                }
                $("#form").append("<div class='p-2'>\n" +
                    "            <h4 class='text-center'>Profile Summary</h4>\n" +
                    "            <table object='" + profileSummaryDTOList + "' border='0' class='smaller-font table-profile table-striped-custom mt-3 mb-3' style='width:100%;'>\n" +
                    "                <thead>\n" +
                    "                    <tr>\n" +
                    "                        <th class='pl-3' style='width: 40%'>Component Name</th>\n" +
                    "                        <th class='text-center' style='width: 20%'>Required Point(%)</th>\n" +
                    "                        <th class='text-center' style='width: 20%'>Self Point(%)</th>\n" +
                    "                        <th class='text-center' style='width: 20%'>Reviewed Point(%)</th>\n" +
                    "                    </tr>\n" +
                    "                </thead>\n" +
                    "                <tbody>\n" +
                    "                        " + radarList + "\n" +
                    "                        <tr><td class='pl-3'><b>Total</b></td>\n" +
                    "                        <td class='text-center'><b>" + Math.round(patternWeight * 100) / 100 + "</b></td>\n" +
                    "                        <td class='text-center'><b>" + Math.round(selfWeight * 100) / 100 + "</b></td>\n" +
                    "                        <td class='text-center'><b>" + Math.round(reviewWeight * 100) / 100 + "</b></td></tr>\n" +
                    "                </tbody>\n" +
                    "            </table>\n" +
                    "        </div>\n" +
                    "        <div class='p-2'>\n" +
                    "            <h4 class='text-center'>Profile Radar Chart</h4>\n" +
                    "            <canvas id='marksChart' width='400' height='400'></canvas>\n" +
                    "            <div id='btnDetails' class='mt-3 text-center'>" + btnSubmit + "\n" +
                    "                <a class='btn btn-primary ml-2' href='/my-profiles' role='button' >Return</a>" +
                    "            </div>");
                // Radar Chart
                let componentNameList = [];
                let selfRankWeightList = [];
                let reviewRankWeightList = [];
                let patternRankWeightList = [];
                $.each(profileSummaryDTOList, function (index, value) {
                    componentNameList.push(value.competencyComponents.componentName);
                    patternRankWeightList.push(100);
                    if (parseFloat(value.patternRankWeight) !== 0) {
                        let selfRankPoint;
                        let reviewRankPoint;
                        if (parseFloat(value.selfRankWeight) <= parseFloat(value.patternRankWeight)) {
                            selfRankPoint = parseFloat(value.selfRankWeight) * 100 / parseFloat(value.patternRankWeight);
                        } else {
                            selfRankPoint = 100;
                        }
                        if (parseFloat(value.reviewRankWeight) <= parseFloat(value.patternRankWeight)) {
                            reviewRankPoint = parseFloat(value.reviewRankWeight) * 100 / parseFloat(value.patternRankWeight);
                        } else {
                            reviewRankPoint = 100;
                        }
                        selfRankWeightList.push(selfRankPoint);
                        reviewRankWeightList.push(reviewRankPoint);
                    } else {
                        selfRankWeightList.push(100);
                        reviewRankWeightList.push(100);
                    }
                });
                let marksCanvas = document.getElementById("marksChart");
                let options = {
                    scales: {
                        r: {
                            angleLines: {
                                display: true,
                            },
                            suggestedMin: 0,
                        }
                    }
                };
                marksData = {
                    labels: componentNameList,
                    datasets: [
                        {
                            label: "Pattern-Rank",
                            pointStyle: 'circle',
                            pointBorderWidth: 2,
                            borderDash: [3, 5],
                            pointBackgroundColor: "rgb(227,0,0)",
                            borderWidth: 2,
                            borderColor: "rgba(226,0,0,0.5)",
                            backgroundColor: "rgba(200,0,0,0.03)",
                            data: patternRankWeightList,
                        },
                        {
                            label: "Review-Rank",
                            pointStyle: 'circle',
                            pointBorderWidth: 2,
                            borderDash: [3, 5],
                            pointBackgroundColor: "rgb(200,93,0)",
                            borderWidth: 2,
                            borderColor: "rgb(255,132,0)",
                            backgroundColor: "rgba(0,0,200,0.03)",
                            data: reviewRankWeightList,
                        },
                        {
                            label: "Self-Rank",
                            pointStyle: 'circle',
                            pointBorderWidth: 2,
                            borderDash: [3, 5],
                            pointBackgroundColor: "rgba(0,0,200,1)",
                            borderWidth: 2,
                            borderColor: "rgba(0,0,200,0.5)",
                            backgroundColor: "rgba(200,0,0,0.03)",
                            data: selfRankWeightList,
                        }
                    ]
                };
                let radarChart = new Chart(marksCanvas, {type: 'radar', data: marksData, options: options});
            }
        })
    })
}

// Get list Pattern to create Pattern in my-profile/create
function getListPattern() {
    let periodId = $("#periodId").val();
    let nextUrl = '/my-profiles/list-pattern/' + periodId;
    $.ajax({
        url: nextUrl,
        type: "get",
        async: false,
        success: function (list) {
            if (list == '') {
                $("#patternId").replaceWith("<select id=\"patternId\" name=\"competencyPatternId\" required style=\"width: 100%\"><option value=\"\">No Pattern for this Period</option></select>");
            } else {
                let val;
                let result = "";
                list.forEach(patternDTO => {
                    val = "<option value=\"" + patternDTO.competencyPatternId + "\">" +
                        "<b>" + patternDTO.jobRoles.jobRoleName + " " + patternDTO.jobRoles.domains.domainName + "</b></option>";
                    result = result + val;
                })
                $("#patternId").replaceWith("<select id=\"patternId\" name=\"competencyPatternId\" required style=\"width: 100%\">" + result + "</select>");
            }
        }
    })
}

// Delete a Profile
function deleteProfile(rankingProfileId) {
    $(document).ready(function () {
        if (deleteConfirmDialog() == false) {
            return;
        }
        let nextUrl = "/my-profiles/delete/" + rankingProfileId;
        let page = 0;
        if ($("#page")) {
            page = $("#page").val();
        }
        $.ajax({
            url: nextUrl,
            type: 'get',
            data: {page: page},
            async: false,
            success: function (result) {
                searchList(page);
            }
        })
    })
}

// Confirm dialog box to delete
function deleteConfirmDialog() {
    let result = confirm("Do you want to delete Profile?");
    if (result) {
        return true;
    } else {
        return false;
    }
}

// Confirm dialog box to Save
function saveConfirmDialog() {
    let result = confirm("Do you want to save this Profile?");
    if (result) {
        return true;
    } else {
        return false;
    }
}

function actionSubmitProfile(idProfile, action) {
    let confirmContent = "Do you want to " + action + " this Profile?";
    if (confirm(confirmContent)) {
        window.location = "http://localhost:8080/my-profiles/submitted/" + idProfile;
    }
}

function resetProfileFilter() {
    $("#title").val('');
    $("#period").val('');
    $("#created").val('');
    $("#stats").val('');
    $("#roles").val('');
    searchList();
}