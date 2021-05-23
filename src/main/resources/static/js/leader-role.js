function showMemberProfiles(index) {
    let nameEmployee = document.getElementById("employeeIndex" + index).innerHTML;
    let nameAlreadySearch = $("#memberName").val();
    if (nameEmployee !== nameAlreadySearch) {
        $("#employeeNameContainer").empty();
        $("#employeeNameContainer").append("<input type=\"text\" id=\"memberName\" placeholder=\"Member Name...\" value=\"" + nameEmployee + "\"/>");
        filterMemberProfile();
    }
};

function transferCompetencyComponent(idProfile, sttComponent) {
    let nextUrl = "/member-profiles/rankingProfile/" + idProfile + "/" + sttComponent;
    $.ajax({
        url: nextUrl,
        async: false,
        success: function (reviewPageInfoDTO) {
            let profileInformation = getProfileInformation(reviewPageInfoDTO);
            let dotContent = "";
            let competencyComponentsList = reviewPageInfoDTO.crProfileDetailAndReviewLevelList[0].competencyRankingProfileDetails.competencyRankingPatternDetails.competencyRankingPatterns.competencyComponentsList;
            $.each(competencyComponentsList, function (cComponentIndex, cComponent) {
                if (cComponentIndex === sttComponent) {
                    dotContent += "---<span title=\"" + cComponent.componentName + "\" class=\"hoverHand\">🔴</span>";
                } else {
                    let dotStatus;
                    if (reviewPageInfoDTO.listIndexUnreviewedCComponent.includes(cComponentIndex)) {
                        dotStatus = "⭕";
                    } else {
                        dotStatus = "🔘";
                    }
                    dotContent += "---<span title=\"" + cComponent.componentName + "\" onclick=\"transferCompetencyComponent(" + idProfile + "," + cComponentIndex + ")\" class=\"hoverHand\">" + dotStatus + "</span>";
                }
            });
            dotContent += "---<span title=\"Profile Summary\" onclick=\"showSummaryProfileToReview(" + idProfile + ")\"  class=\"hoverHand\">🔘</span>---";

            let profileDetailTitle = "           <thead>\n" +
                "               <tr style=\" height: 52px;\">\n" +
                "                    <th class=\"pl-3\"  style=\"width:30%;\">" + reviewPageInfoDTO.crProfileDetailAndReviewLevelList[0].competencyRankingProfileDetails.competencyRankingPatternDetails.competencyComponentDetails.competencyComponents.componentName + "</th>\n" +
                "                    <th class=\"text-center p-3\" style=\"width:15%;\">Source Information</th>\n" +
                "                    <th class=\"text-center \" style=\"width:15%;\">R.Weight(%)</th>\n" +
                "                    <th class=\"text-center\" style=\"width:20%;\">Self.Ranked</th>\n" +
                "                    <th class=\"text-center\" style=\"width:20%;\">Review Level</th>\n" +
                "                </tr>\n" +
                "           </thead>\n";

            let profileDetailContent = "";
            $.each(reviewPageInfoDTO.crProfileDetailAndReviewLevelList, function (pDetailIndex, competencyRankingProfileDetail) {
                let option = "";
                let select = "";
                $.each(competencyRankingProfileDetail.competencyRankingProfileDetails.competencyRankingPatternDetails.competencyComponentDetails.proficiencyLevelTypes.proficiencyLevelsList, function (pLevelIndex, pLevel) {
                    if (competencyRankingProfileDetail.competencyReviewRankings.proficiencyLevels != null) {
                        if (competencyRankingProfileDetail.competencyReviewRankings.proficiencyLevels.proficiencyLevel === pLevel.proficiencyLevel) {
                            select = "selected";
                        }
                    }
                    option += "<option value=\"" + pLevel.proficiencyLevelId + "\"" + select + ">" + pLevel.proficiencyLevelName + "</option>\n";
                    select = "";
                });

                let sourceInformation = "";
                if (competencyRankingProfileDetail.competencyRankingProfileDetails.dataSources != null) {
                    sourceInformation = competencyRankingProfileDetail.competencyRankingProfileDetails.dataSources.dataSourceName;
                }
                profileDetailContent += "<tr style=\" \">\n" +
                    "        <td class=\"pl-3\">\n" +
                    "            <span>" + competencyRankingProfileDetail.competencyRankingProfileDetails.competencyRankingPatternDetails.competencyComponentDetails.componentDetailName + "</span>\n" +
                    "        </td>\n" +
                    "        <td class=\"text-center\">" + sourceInformation + "</td>\n" +
                    "        <td class=\"text-center\">" + competencyRankingProfileDetail.competencyRankingProfileDetails.competencyRankingPatternDetails.rankingWeight + "</td>\n" +
                    "        <td class=\"pl-2\">" + competencyRankingProfileDetail.competencyRankingProfileDetails.proficiencyLevels.proficiencyLevelName + "</td>\n" +
                    "        <td>\n" +
                    "            <select style=\"width:100%;\" id=\"proficiencyLevel" + (competencyRankingProfileDetail.competencyRankingProfileDetails.rankingProfileDetailId - reviewPageInfoDTO.crProfileDetailAndReviewLevelList[0].competencyRankingProfileDetails.rankingProfileDetailId + 1) + "\">\n" +
                    option +
                    "            </select>\n" +
                    "        </td>\n" +
                    "</tr>\n";
            });

            let tableProfileDetail = "<table border=\"0\" style=\"width:100%;\" class='col-12 mb-2 table-profile table-striped-custom'>\n" +
                profileDetailTitle +
                "      <tbody>\n" +
                profileDetailContent +
                "      </tbody>\n" +
                "</table>";

            let reviewActionButton = "<div class='col-12 space-document'><i>You are now can review and finish member's profile OR review profile and continue to rankings of Business Contributions page.</i></div>\n" +
                "<div class=\"col-12 mt-3 text-center\">\n" +
                "    <input type=\"submit\" role=\"button\" class=\"btn btn-primary mr-1\" value=\"Review &amp; Finish\" style=\"width: fit-content; height: fit-content\" onclick=\"reviewAndFinish(" + idProfile + "," + sttComponent + "," + reviewPageInfoDTO.crProfileDetailAndReviewLevelList.length + ")\"/>\n" +
                "    <input type=\"submit\" role=\"button\" class=\"btn btn-primary\" value=\"Review &amp; Continue\" style=\"width: fit-content; height: fit-content\" onclick=\"reviewAndNext(" + idProfile + "," + sttComponent + "," + reviewPageInfoDTO.crProfileDetailAndReviewLevelList.length + ")\"/>\n" +
                "    <a href=\"/member-profiles\"  class=\"btn btn-primary\" style=\"width: fit-content; height: fit-content\"/>Cancel</a>\n" +
                "</div>";

            let contentBodyRight = "<div class=\"background-content col-lg-8 col-md-12 col-sm-12 p-5 smaller-font profile-right\" style=\"padding:10px;\" id=\"content-body-right\">\n" +
                "   <h2 class=\"text-center pt-4\" style='color: #60a6e4'>" + reviewPageInfoDTO.competencyResults.competencyRankingProfile.employees.fullName + "'s CRF™ Profile</h2>\n" +
                "   <hr>" +
                "   <div class=\"container mt-2\">\n" +
                profileInformation +
                "       <div class=\"\" id=\"competencyComponentDot\">\n" +
                "          <p class=\"text-center\">\n" +
                dotContent +
                "          </p>\n" +
                "       </div>\n" +
                "       <div class=\"content-center\" id=\"profileDetailReview\">\n" +
                "            <div class=\"row\" style=\"padding:10px;\">\n" +
                tableProfileDetail +
                reviewActionButton +
                "            </div>\n" +
                "       </div>\n" +
                "   </div>\n" +
                "</div>"

            $("#content-body-right").remove();
            $("#content-body").append(contentBodyRight);
        },
        error: function () {
            alert("Transfer Component Fail!!!")
        }
    });
};

function showReviewProfileDetail(idProfile) {
    transferCompetencyComponent(idProfile, 0);
};

function showSummaryProfileToReview(idProfile) {
    let nextUrl = "/member-profiles/rankingProfile/profileSummaryAjax/" + idProfile;
    $.ajax({
        url: nextUrl,
        async: false,
        success: function (summaryPageInfoDTO) {
            let accountRole = document.getElementById('accountRole').innerHTML;
            let profileInformation = getProfileInformation(summaryPageInfoDTO);
            let dotContent = "";
            if (document.getElementById('accountRole').innerHTML === "Leader") {
                $.each(summaryPageInfoDTO.competencyResults.competencyRankingProfile.competencyRankingPatterns.competencyComponentsList, function (cComponentIndex, cComponent) {
                    let dotStatus;
                    if (summaryPageInfoDTO.listIndexUnreviewedCComponent.includes(cComponentIndex)) {
                        dotStatus = "⭕";
                    } else {
                        dotStatus = "🔘";
                    }
                    dotContent += "---<span title=\"" + cComponent.componentName + "\" onclick=\"transferCompetencyComponent(" + idProfile + "," + cComponentIndex + ")\" class=\"hoverHand\">" + dotStatus + "</span>";
                });
                dotContent += "---<span title=\"Profile Summary\" onclick=\"showSummaryProfileToReview(" + idProfile + ")\"  class=\"hoverHand\">🔴</span>---";
            }
            let competencyComponentName = [];
            let selfRankWeight = [];
            let reviewRankWeight = [];
            let requireRankWeight = [];
            let summaryDetailInformationHTML = "";
            let totalRequireWeight = 0;
            let totalReviewedRankWeight = 0;
            let totalSelfRankWeight = 0;
            $.each(summaryPageInfoDTO.profileSummaryDTOList, function (profileSummaryDTOIndex, profileSummaryDTO) {
                let requireWeightHTML = "";
                let selfRankWeightHTML = "";
                if (accountRole === "Manager") {
                    requireWeightHTML = "    <td class=\"text-center\">" + profileSummaryDTO.totalWeight + "</td>\n";
                    requireRankWeight.push(100);
                    if (parseInt(profileSummaryDTO.totalWeight) !== 0) {
                        if (parseFloat(profileSummaryDTO.reviewRankWeight) <= parseFloat(profileSummaryDTO.totalWeight)) {
                            reviewRankWeight.push((parseFloat(profileSummaryDTO.reviewRankWeight) * 100) / parseFloat(profileSummaryDTO.totalWeight));
                        } else {
                            reviewRankWeight.push(100);
                        }
                    } else {
                        reviewRankWeight.push(100);
                    }
                } else if (accountRole === "Leader") {
                    selfRankWeightHTML = "    <td class=\"text-center\">" + profileSummaryDTO.selfRankWeight + "</td>\n";

                    if (parseFloat(profileSummaryDTO.reviewRankWeight) !== 0) {
                        selfRankWeight.push((parseFloat(profileSummaryDTO.selfRankWeight) * 100) / parseFloat(profileSummaryDTO.reviewRankWeight));
                        reviewRankWeight.push(100);
                    } else {
                        selfRankWeight.push(100);
                        reviewRankWeight.push(0);
                    }
                }
                summaryDetailInformationHTML += "<tr>\n" +
                    "    <td class=\"pl-3 hoverHand\" onclick='showDetailReview(this, " + profileSummaryDTO.competencyRankingProfiles.rankingProfileId + ")'>" + profileSummaryDTO.competencyComponents.componentName + "</td>\n" +
                    selfRankWeightHTML +
                    "    <td class=\"text-center\">" + profileSummaryDTO.reviewRankWeight + "</td>\n" +
                    requireWeightHTML +
                    "</tr>\n";
                competencyComponentName.push(profileSummaryDTO.competencyComponents.componentName);
                totalRequireWeight += parseFloat(profileSummaryDTO.totalWeight);
                totalReviewedRankWeight += parseFloat(profileSummaryDTO.reviewRankWeight);
                totalSelfRankWeight += parseFloat(profileSummaryDTO.selfRankWeight);
            });
            let totalSelfRankWeightHTML = "";
            let totalRequireWeightHTML = "";
            let thSelfRankWeightHTML = "";
            let thRequireWeightHTML = "";
            let labelBlue = "";
            let labelRed = "";
            let dataBlue = [];
            let dataRed = [];
            if (accountRole === "Manager") {
                thRequireWeightHTML = "               <th class=\"text-center\" style=\"width:30%;\">Required Point(%)</th>\n";
                totalRequireWeightHTML = "               <td class=\"text-center\"><b>" + Math.round(totalRequireWeight * 100) / 100 + "</b></td>\n";
                labelBlue = "Reviewed Rank";
                labelRed = "Require Rank";
                dataBlue = reviewRankWeight;
                dataRed = requireRankWeight;
            } else if (accountRole === "Leader") {
                thSelfRankWeightHTML = "               <th class=\"text-center\" style=\"width:30%;\">Self Point(%)</th>\n";
                totalSelfRankWeightHTML = "               <td class=\"text-center\"><b>" + Math.round(totalSelfRankWeight * 100) / 100 + "</b></td>\n";
                labelBlue = "Self Rank";
                labelRed = "Reviewed Rank";
                dataBlue = selfRankWeight;
                dataRed = reviewRankWeight;
            }
            let totalRankWeightHTML = "           <tr>\n" +
                "               <td class=\"pl-3\"><b>Total</b></td>\n" +
                totalSelfRankWeightHTML +
                "               <td class=\"text-center\"><b>" + Math.round(totalReviewedRankWeight * 100) / 100 + "</b></td>\n" +
                totalRequireWeightHTML +
                "           </tr>\n";

            let tableSummaryHTML = "<table border=\"0\" class=\"table-profile table-striped-custom mt-4 mb-3\" style=\"width:100%;\" id='summaryReviewTable'>\n" +
                "    <thead>\n" +
                "        <tr >\n" +
                "            <th class=\"pl-3\" style=\"width:40%;\">Component Name</th>\n" +
                thSelfRankWeightHTML +
                "            <th class=\"text-center p-3\" style=\"width:30%;\">Reviewed Point(%)</th>\n" +
                thRequireWeightHTML +
                "        </tr>\n" +
                "    </thead>\n" +
                "    <tbody>\n" +
                summaryDetailInformationHTML +
                totalRankWeightHTML +
                "    </tbody>\n" +
                "</table>\n";
            let reviewedComment = "";
            if (summaryPageInfoDTO.competencyResults.cmtAction != null) {
                reviewedComment = summaryPageInfoDTO.competencyResults.cmtAction;
            }
            let reviewDescriptionHTML = "<p class=\"\">\n" +
                "    <div class='mb-2'>Review Description</div>\n" +
                "    <textarea cols=\"40\" id=\"reviewNote\" rows=\"3\">" + reviewedComment + "</textarea>\n" +
                "</p>\n";
            let contentBodyRightHTML = "<div class=\"background-content col-lg-8 col-md-12 col-sm-12 p-5 smaller-font profile-right\" id=\"content-body-right\">\n" +
                "   <h2 class=\"text-center pt-4\"  style='color: #60a6e4'>" + summaryPageInfoDTO.competencyResults.competencyRankingProfile.employees.fullName + "'s CRF™ Profile</h2>\n" +
                "   <hr>\n" +
                "   <div class=\"container mt-2\">\n" +
                profileInformation +
                "       <div class=\"\" id=\"competencyComponentDot\">\n" +
                "            <p class=\"text-center\">\n" +
                dotContent +
                "            </p>\n" +
                "       </div>\n" +
                "       <div class=\"row\" id=\"summaryProfile\">\n" +
                "            <div class=\"p-2 col-12\">\n" +
                "               <h4 class=\"text-center \">Review Table</h4>\n" +
                tableSummaryHTML +
                "            </div>\n" +
                "            <div class=\"p-2 col-12\">\n" +
                "               <h4 class=\"text-center\">Review Radar Chart</h4>\n" +
                "               <canvas id=\"marksChart\" width=\"400\" height=\"400\" class=\"mt-2\"></canvas>\n" +
                "               <br>\n" +
                "            </div>" +
                "       </div>\n" +
                "       <div class=\"row\">\n" +
                "            <div class=\"col-12\">\n" +
                reviewDescriptionHTML +
                "                <div class='space-document'><i>You are now finished reviewing member's CRF™ profile and please check before approve this profile.</i></div>\n" +
                "                <div class=\"mt-3 text-center\">\n" +
                "                   <input class=\"btn btn-primary mb-3\" type=\"submit\" value=\"Approve Profile\" style=\"width: fit-content; height: fit-content\" onclick=\"approveSubmittedProfile(" + idProfile + ")\"/>\n" +
                "                   <input class=\"btn btn-warning mb-3\" type=\"submit\" value=\"Reject Profile\" style=\"width: fit-content; height: fit-content\"  onclick=\"rejectSubmittedProfile(" + idProfile + ")\"/>\n" +
                "                   <a href=\"/member-profiles/\"  class=\"btn btn-info mb-3\" style=\"width: fit-content; height: fit-content\"/>Continue Review</a>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "   </div>\n" +
                "</div>";
            $("#content-body-right").remove();
            $("#content-body").append(contentBodyRightHTML);
            let marksCanvas = document.getElementById("marksChart");
            let marksData = {
                labels: competencyComponentName,
                datasets: [
                    {
                        label: labelBlue,
                        pointStyle: 'circle',
                        pointBorderWidth: 2,
                        borderDash: [3, 5],
                        pointBackgroundColor: "rgba(0,0,200,1)",
                        borderWidth: 2,
                        borderColor: "rgba(0,0,200,0.5)",
                        backgroundColor: "rgba(200,0,0,0.03)",
                        data: dataBlue
                    },
                    {
                        label: labelRed,
                        pointStyle: 'circle',
                        pointBorderWidth: 2,
                        borderDash: [3, 5],
                        pointBackgroundColor: "rgba(200,0,0,1)",
                        borderWidth: 2,
                        borderColor: "rgba(200,0,0,0.5)",
                        backgroundColor: "rgba(0,0,200,0.03)",
                        data: dataRed
                    }
                ]
            };
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
            let radarChart = new Chart(marksCanvas, {type: 'radar', data: marksData, options: options});
        },
        error: function () {
            window.alert("summaryProfile Fail")
        }
    });
};

function reviewAndFinish(idProfile, sttComponent, sizeOfListDetail) {
    saveReviewProfileDetail(idProfile, sttComponent, sizeOfListDetail);
    showSummaryProfileToReview(idProfile);
};

function reviewAndNext(idProfile, sttComponent, sizeOfListDetail) {
    let canNext = saveReviewProfileDetail(idProfile, sttComponent, sizeOfListDetail);
    if (canNext === true) {
        sttComponent += 1;
        transferCompetencyComponent(idProfile, sttComponent);
    } else {
        showSummaryProfileToReview(idProfile);
    }
};

function saveReviewProfileDetail(idProfile, sttComponent, sizeOfListDetail) {
    let levelRank = [];
    for (let i = 1; i <= sizeOfListDetail; i++) {
        levelRank.push($("#proficiencyLevel" + i).val());
    }
    ;
    let data = {
        "sttOfCompetencyComponent": sttComponent,
        "idProfile": idProfile,
        "levelReviewList": levelRank
    };
    let canNextComponent = false;
    $.ajax({
        type: 'POST',
        dataType: 'json',
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(data),
        url: '/member-profiles/rankingProfile/review',
        async: false,
        success: function (canNext) {
            canNextComponent = canNext;
        },
        fail: function () {
            alert("Save Review Fail !!!")
        }
    });
    return canNextComponent;
}

function getProfileInformation(pageInfoDTO) {
    let profileInformation = "<div class=\"row\">\n" +
        "    <div class=\"col-lg-7 col-md-12 col-sm-12\">\n" +
        "        <span>Profile Information: </span>\n" +
        "        <ul class=\" \">\n" +
        "            <li>Period: <b>" + pageInfoDTO.competencyResults.competencyRankingProfile.competencyRankingPatterns.periodPattern.name + "-"
        + pageInfoDTO.competencyResults.competencyRankingProfile.competencyRankingPatterns.periodPattern.year + "</b></li>\n" +
        "            <li>Current Rank: <b>" + pageInfoDTO.competencyResults.competencyRankingProfile.employees.jobRoles.jobRoleName + " "
        + pageInfoDTO.competencyResults.competencyRankingProfile.employees.domains.domainName + "</b></li>\n" +
        "            <li>Submitted Ranked: <b><i>" + pageInfoDTO.competencyResults.competencyRankingProfile.competencyRankingPatterns.jobRoles.jobRoleName + " "
        + pageInfoDTO.competencyResults.competencyRankingProfile.competencyRankingPatterns.jobRoles.domains.domainName + "</i></b></li>\n" +
        "            <li>Self Ranked Points: <b>" + pageInfoDTO.competencyResults.competencyRankingProfile.selfRakingPoints + "</b></li>\n" +
        "        </ul>\n" +
        "    </div>\n" +
        "    <div  class=\"col-lg-5 col-md-12 col-sm-12\">\n" +
        "        <span>Review Information: </span>\n" +
        "        <ul class=\"\">\n" +
        "            <li>Reviewer: <b><i>" + pageInfoDTO.competencyResults.reviewer.fullName + "</i></b></li>\n" +
        "            <li>Profile Status : <b>" + pageInfoDTO.competencyResults.competencyRankingProfile.statusTypes + "</b></li>\n" +
        "            <li>Review Points: <b>" + Math.round(pageInfoDTO.competencyResults.reviewRakingPoints * 100) / 100 + "</b></li>\n" +
        "        </ul>\n" +
        "    </div>\n" +
        "</div>";
    return profileInformation;
}

function approveSubmittedProfile(idProfile) {
    let approveDescription = $("#reviewNote").val();
    if (approveDescription === "") {
        alert("Describe profile before Approve !!!");
    } else {
        let nextUrl = "/member-profiles/approveSubmittedProfile/" + idProfile + "?cmtApprove=" + approveDescription;
        $.ajax({
            url: nextUrl,
            async: false,
            success: function (approvedStatus) {
                alert(approvedStatus);
                window.location = "http://localhost:8080/member-profiles/";
            },
            error: function (approvedStatus) {
                alert(approvedStatus.responseText);
            },
        });
    }
}

function rejectSubmittedProfile(idProfile) {
    let rejectDescription = $("#reviewNote").val();
    let nextUrl = "/member-profiles/rejectSubmittedProfile/" + idProfile + "?cmtReject=" + rejectDescription;
    let returnStatus;
    $.ajax({
        url: nextUrl,
        async: false,
        success: function (rejectedStatus) {
            alert(rejectedStatus);
            window.location = "http://localhost:8080/member-profiles/"
        },
        error: function (rejectedStatus) {
            alert(rejectedStatus.responseText);
        },
    });
}

function printMemberProfiles() {
    let mywindow = window.open('', 'PRINT', 'height=650,width=900,top=100,left=150');
    mywindow.document.write("<html><head><title></title>");
    mywindow.document.write("<link href=\"/css/totalcss/total.css\" rel=\"stylesheet\">");
    mywindow.document.write("<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css\" integrity=\"sha384-B0vP5xmATw1+K9KRQjQERJvTumQW0nPEzvF6L/Z6nronJ3oUOFUFpCjEUQouq2+l\" crossorigin=\"anonymous\">");
    mywindow.document.write("</head><body>");
    mywindow.document.write(document.getElementById("content-body-right").innerHTML);
    mywindow.document.write("</body></html>");
    mywindow.document.close(); // necessary for IE >= 10
    mywindow.focus(); // necessary for IE >= 10*/
    setTimeout(function () {
        mywindow.print();
        mywindow.close();
    }, 100);
}

function printProfile(idProfile) {
    let nextUrl = "/member-profiles/printProfileAjax/" + idProfile;
    $.ajax({
        url: nextUrl,
        async: false,
        success: function (cRProfileReviewAndResultDTO) {
            let employeeName = "   <h4 class=\"\">" +
                cRProfileReviewAndResultDTO.competencyResults.competencyRankingProfile.employees.fullName + "'s CRF™ Profile</h4>\n";
            let profileInformation = "<div class='m-0 p-0 mt-3'>" +
                getProfileInformation(cRProfileReviewAndResultDTO) +
                "</div>";
            let profileReviewInformation = "";
            $.each(cRProfileReviewAndResultDTO.crProfileDetailWithComponentList, function (crProfileDetailWithComponentIndex, crProfileDetailWithComponent) {
                let profileDetailTitle = "           <thead>\n" +
                    "               <tr>\n" +
                    "                    <th class=\"text-center p-3\">" + crProfileDetailWithComponent.competencyComponents.componentName + "</th>\n" +
                    "                    <th class=\"text-center\">Source Information</th>\n" +
                    "                    <th class=\"text-center\"><b>" + crProfileDetailWithComponent.totalWeight + "(%) </b></th>\n" +
                    "                    <th class=\"text-center\">Self.Ranked</th>\n" +
                    "                    <th class=\"text-center\" style=\"width:135px;\">Review Level</th>\n" +
                    "                </tr>\n" +
                    "           </thead>\n";

                let profileDetailContent = "";
                $.each(crProfileDetailWithComponent.crProfileDetailAndReviewLevelList, function (crProfileDetailAndReviewLevelIndex, crProfileDetailAndReviewLevel) {
                    let datasource;
                    if (crProfileDetailAndReviewLevel.competencyRankingProfileDetails.dataSources == null) {
                        datasource = "";
                    } else {
                        datasource = crProfileDetailAndReviewLevel.competencyRankingProfileDetails.dataSources.dataSourceName;
                    }
                    let reviewProficiencyLevels = "";
                    if (crProfileDetailAndReviewLevel.competencyReviewRankings.proficiencyLevels != null) {
                        reviewProficiencyLevels = crProfileDetailAndReviewLevel.competencyReviewRankings.proficiencyLevels.proficiencyLevelName;
                    }
                    profileDetailContent += "   <tr>\n" +
                        "        <td class=\"pl-4\">\n" +
                        "            <span>" + crProfileDetailAndReviewLevel.competencyRankingProfileDetails.competencyRankingPatternDetails.competencyComponentDetails.componentDetailName + "</span>\n" +
                        "        </td>\n" +
                        "        <td class=\"pl-4\">" + datasource + "</td>\n" +
                        "        <td class=\"text-center\">" + crProfileDetailAndReviewLevel.competencyRankingProfileDetails.competencyRankingPatternDetails.rankingWeight + "</td>\n" +
                        "        <td class=\"pl-4\">" + crProfileDetailAndReviewLevel.competencyRankingProfileDetails.proficiencyLevels.proficiencyLevelName + "</td>\n" +
                        "        <td class=\"pl-4\">" + reviewProficiencyLevels + "</td>\n" +
                        "   </tr>\n";
                });
                profileReviewInformation += profileDetailTitle + profileDetailContent;
            });
            let tableProfileDetail = "  <table border=\"1\" style=\"width:100%;\" class=\"table-profile table-striped-custom\">\n" +
                profileReviewInformation +
                "  </table>";

            let mywindow = window.open('', 'PRINT', 'height=650,width=900,top=100,left=150');
            mywindow.document.write("<html><head><title></title>");
            mywindow.document.write("<link href=\"/css/totalcss/total.css\" rel=\"stylesheet\">");
            mywindow.document.write("<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css\" integrity=\"sha384-B0vP5xmATw1+K9KRQjQERJvTumQW0nPEzvF6L/Z6nronJ3oUOFUFpCjEUQouq2+l\" crossorigin=\"anonymous\">");
            mywindow.document.write("</head><body>");
            mywindow.document.write(employeeName);
            mywindow.document.write(profileInformation);
            mywindow.document.write(tableProfileDetail);
            mywindow.document.write("</body></html>");
            mywindow.document.close(); // necessary for IE >= 10
            mywindow.focus(); // necessary for IE >= 10*/
            setTimeout(function () {
                mywindow.print();
                mywindow.close();
            }, 300);
        }, error: function () {
            alert("Print Fail!!!")
        }
    });
}

function filterMemberProfile() {
    let day = $("#day").val();
    let domainName = $("#domainName").val();
    // To not catch nullpointer exception when check this value in function getMemberProfileByFilter of Competency Ranking Profile Service
    if (domainName === undefined) {
        domainName = "";
    }
    let jobRoleId = $("#jobRoleId").val();
    let periodId = $("#periodId").val();
    let memberName = $("#memberName").val();
    let status = $("#status").val();
    let data = {
        "jobRoleId": jobRoleId,
        "periodId": periodId,
        "memberName": memberName,
        "submittedDate": day,
        "status": status,
        "domainName": domainName
    };
    $.ajax({
        type: 'POST',
        dataType: 'json',
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(data),
        url: '/member-profiles/filterProfile',
        async: false,
        success: function (filterMemberProfileResult) {
            appendMemberProfileFilterResult(filterMemberProfileResult);
        },
        fail: function () {
            alert("Filter Fail !!!")
        }
    });
}

function changePage(page) {
    $.ajax({
        type: 'GET',
        dataType: 'json',
        contentType: "application/json;charset=utf-8",
        url: '/member-profiles/' + page,
        async: false,
        success: function (filterMemberProfileResult) {
            appendMemberProfileFilterResult(filterMemberProfileResult);
        },
        fail: function () {
            alert("Change Page Fail !!!")
        }
    });
}

function appendMemberProfileFilterResult(filterMemberProfileResult) {
    let profilesHTML = "";
    let accountRole = document.getElementById('accountRole').innerHTML;
    $.each(filterMemberProfileResult.competencyRankingProfilesFilterList, function (crProfileFilterIndex, memberRankingProfile) {
        let styleStatusProfile = "";
        let dayTitle;
        let dayValue;
        let reviewIcon = "";
        if (((memberRankingProfile.statusTypes) === "ReviewedFail") || ((memberRankingProfile.statusTypes) === "Rejected")) {
            styleStatusProfile = "style=\"color: #e4606d\"";
        } else if ((memberRankingProfile.statusTypes) === "Approved") {
            styleStatusProfile = "style=\"color: #419def\"";
        }
        if (accountRole === "Leader") {
            if ((memberRankingProfile.statusTypes) === "Reviewed") {
                styleStatusProfile = "style=\"color: #419def\"";
            }
            dayTitle = memberRankingProfile.submittedDate;
            dayValue = (new Date(dayTitle)).toLocaleDateString('en-GB', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit'
            });
            reviewIcon = "        <span onclick=\"showReviewProfileDetail(" + memberRankingProfile.rankingProfileId + ")\" class=\"hoverHand\">\n" +
                "            <img src=\"/icons/reviewP.png\" title=\"Review\" class=\"mini-icon2\">\n" +
                "        </span>\n"
        } else if (accountRole === "Manager") {
            dayTitle = memberRankingProfile.competencyResults.reviewedDate;
            dayValue = (new Date(dayTitle)).toLocaleDateString('en-GB', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit'
            });
        }
        profilesHTML += "<tr>\n" +
            "    <td class=\"pl-2\">" + memberRankingProfile.competencyRankingPatterns.jobRoles.jobRoleName + " " + memberRankingProfile.employees.domains.domainName + "</td>\n" +
            "    <td class=\"text-center\">" + memberRankingProfile.competencyRankingPatterns.periodPattern.name + " " + memberRankingProfile.competencyRankingPatterns.periodPattern.year + "</td>\n" +
            "    <td class=\"text-center\">\n" +
            "        <span id=\"employeeIndex" + crProfileFilterIndex + "\" class=\"hoverHand\" onclick=\"showMemberProfiles(" + crProfileFilterIndex + ")\" style=\"color: #d39e00\">" +
            memberRankingProfile.employees.fullName +
            "</span>\n" +
            "    </td>\n" +
            "    <td class=\"text-center\"><span title=\"" + dayTitle + "\">" + dayValue + "</span></td>" +
            "    <td class=\"pl-2\" " + styleStatusProfile + ">" + memberRankingProfile.statusTypes + "</td>\n" +
            "    <td class=\"text-center\">\n" +
            reviewIcon +
            "        <span onclick=\"showSummaryProfileToReview(" + memberRankingProfile.rankingProfileId + ")\" class=\"hoverHand\">\n" +
            "            <img src=\"/icons/summary.png\" title=\"Summary\" class=\"mini-icon2\">\n" +
            "        </span>\n" +
            "        <span onclick=\"printProfile(" + memberRankingProfile.rankingProfileId + ")\" class=\"hoverHand\">\n" +
            "            <img src=\"/icons/print.png\" title=\"Print\" class=\"mini-icon2\">\n" +
            "        </span>\n" +
            "    </td>\n" +
            "</tr>\n"
    });
    let emptyTr = "";
    let timeLoopEmptyTh = 5;
    if (filterMemberProfileResult.competencyRankingProfilesFilterList.length === 0) {
        emptyTr = "<tr class='profilesList'>" +
            "<td class='text-center' colspan=\"6\" style='height: 31px'>Have No Member Profile</td>" +
            "</tr>";
        timeLoopEmptyTh = 4;
    }
    for (let i = 0; i < (timeLoopEmptyTh - filterMemberProfileResult.competencyRankingProfilesFilterList.length); i++) {
        emptyTr += "<tr style=\"height: 31px\" class=\"empty-tr profilesList\">\n" +
            "    <td ></td> <td></td> <td></td> <td></td> <td></td> <td></td>\n" +
            "</tr>\n";
    }
    profilesHTML += emptyTr;
    let listPage = "";
    $.each(filterMemberProfileResult.listPage, function (pageIndex, page) {
        let currentPageColor = "style=\"color: gray; font-size: 90%\"";
        let onclick = " onclick=\"changePage(" + page + ")\"";
        if (page === filterMemberProfileResult.currentPage) {
            currentPageColor = "style=\"color: black\"";
            onclick = "";
        }
        listPage += "<span class=\"hoverHand\" " + currentPageColor + onclick + ">" + page + "</span>\n";
    });
    let returnBTAndListPageHTML = "<div class=\"mt-3\">\n" +
        "    <a class=\"btn btn-primary a-block ml-3 button-small-height2 pt-1\" href=\"/member-profiles/return\" style=\"font-size: 100%; padding-top: 3px\">Return</a>\n" +
        "    <span class=\"float-right mt-2 mr-3\" style='font-size: 150%'>\n" +
        "        ...\n" +
        listPage +
        "        ...\n" +
        "    </span>\n" +
        "</div>";
    let dayTh = "Submitted Day";
    if (accountRole === "Manager") {
        dayTh = "Reviewed Day";
    }
    let filterMemberProfileHTML = "   <table border=\"0\" class=\"smaller-font table-profile table-striped-custom\" id=\"employee-profiles\">\n" +
        "       <thead>\n" +
        "       <tr>\n" +
        "           <th class=\"pl-2\" style='width: 28%'>Job Title</th>\n" +
        "           <th class=\"text-center p-3\" style='width: 10%'>Period</th>\n" +
        "           <th class=\"text-center\" style='width: 15%'>Member/Staff</th>\n" +
        "           <th class=\"text-center\" style='width: 20%'>" + dayTh + "</th>\n" +
        "           <th class=\"text-center\" style='width: 15%'>Status</th>\n" +
        "           <th class=\"text-center\" style=\"width: 12%\">Action</th>\n" +
        "       </tr>\n" +
        "       </thead>\n" +
        "       <tbody>\n" +
        profilesHTML +
        "       </tbody>\n" +
        "   </table>\n" +
        returnBTAndListPageHTML;
    $("#member-profile").empty();
    $("#member-profile").append(filterMemberProfileHTML);
}

let checkComponentProfileIsClickOrNot = [];

function showDetailReview(location, idProfile) {
    let componentName = $(location).html();
    let index = $(location).parent().index();
    let accountRole = document.getElementById('accountRole').innerHTML;
    let nextUrl = "/member-profiles/component-details/" + idProfile + "/" + componentName + "/" + accountRole;
    if (!checkComponentProfileIsClickOrNot.includes(componentName)) {
        checkComponentProfileIsClickOrNot.push(componentName);
        $.ajax({
            url: nextUrl,
            type: "get",
            success: function (result) {
                let val = '';
                let detail = '';
                let idDetails = "sub " + componentName;
                if (accountRole === 'Leader') {
                    result.forEach(result => {
                        val = "<tr id=\"sub\" style='font-size: 90%'>" +
                            "   <td class='pl-4' style='color: #a5a5a5; background-color: #fffff1'>" + result.name + "</td>" +
                            "   <td class=\"text-center\" style='color: #a5a5a5; background-color: #fffff1'>" + result.pointDetails + "</td>" +
                            "   <td class=\"text-center\" style='color: #a5a5a5; background-color: #fffff1'>" + result.reviewPointDetails + "</td>" +
                            "</tr>";
                        detail = detail + val;
                    });
                } else {
                    result.forEach(result => {
                        val = "<tr id=\"sub\" style='font-size: 90%'>" +
                            "   <td class='pl-4' style='color: #a5a5a5; background-color: #fffff1'>" + result.name + "</td>" +
                            "   <td class=\"text-center\" style='color: #a5a5a5; background-color: #fffff1'>" + result.reviewPointDetails + "</td>" +
                            "   <td class=\"text-center\" style='color: #a5a5a5; background-color: #fffff1'>" + result.requirePointDetails + "</td>" +
                            "</tr>";
                        detail = detail + val;
                    });
                }
                $('#summaryReviewTable > tbody > tr').eq(index).after(detail);
                $('[id$="sub"]').attr('id', idDetails.replace(/\s+/g, ''));
            }
        });
    } else {
        let idDetails = ("sub " + componentName).replace(/\s+/g, '');
        $('[id$="' + idDetails + '"]').remove();
        checkComponentProfileIsClickOrNot.splice($.inArray(componentName, checkComponentProfileIsClickOrNot), 1);
    }
}

function resetFilterMemberProfile() {
    $("#jobRoleId").val('');
    $("#domainName").val('');
    $("#periodId").val('');
    $("#memberName").val('');
    $("#day").val('');
    $("#status").val('');
    filterMemberProfile();
}