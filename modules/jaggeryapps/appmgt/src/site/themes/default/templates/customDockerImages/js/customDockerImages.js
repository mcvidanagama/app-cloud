/*
 *
 *   Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 * /
 */


$(document).ready(function () {
    $('#imageUrl').on('focusout keyup blur click', function () { // fires on every keyup & blur
        if ($('#imageUrl').val()) {
            $("#addImage").prop("disabled", false);
        } else {
            $("#addImage").prop("disabled", true);
        }
    });
    fillImagesListTable();
});


/**
 *  Adding new image
 */
function addNewImage() {

    $('#imageUrl').prop("disabled", true);
    $("#addImage").loadingButton({action: 'show'});
    jagg.post("../blocks/customDockerImages//ajax/customDockerImages.jag", {
        action: "isImageAvailable",
        imageUrl: $("#imageUrl").val().trim()
    }, function (result) {
        result = $.trim(result);
        if(result=="false") { // isImageAvailable=false means image is not added currently.

            jagg.post("../blocks/customDockerImages/ajax/customDockerImages.jag", {
                action: "addImageAndCheckSecurity",
                imageUrl: $("#imageUrl").val().trim()
            }, function (result) {
                fillImagesListTable();
                jagg.message({content: 'New Image added and queued for security check.', type: 'info', id: 'addnewcustomdockerimage'});
            }, function (jqXHR, textStatus, errorThrown) {
                jagg.message({
                                 content: jqXHR.responseText,
                                 type: 'error',
                                 id: 'addnewcustomdockerimage',
                                 timeout: 8000
                             });
            });
        } else {
            jagg.message({
                             content: "Cant add image. Image has been already added.",
                             type: 'error',
                             id: 'addnewcustomdockerimage',
                             timeout: 8000
                         });
        }
    }, function (jqXHR, textStatus, errorThrown) {
        jagg.message({
                         content: jqXHR.responseText,
                         type: 'error',
                         id: 'isImageAlreadyAdded',
                         timeout: 8000
                     });
    });
}

function fillImagesListTable() {
    jagg.post("../blocks/customDockerImages/ajax/customDockerImages.jag", {
        action: "getAllImages",
        imageUrl: $("#imageUrl").val().trim()
    }, function (result) {
        var tableHtml = ' <tr><th>Image Url</th><th>Status</th><th>Test Report</th><th>Options</th></tr>';
        var resObj = JSON.parse(result);

        for (i = 0; i < resObj.length; i++) {
            var statusIcon;
            if (resObj[i].status == "passed") {
                statusIcon = '<i class="fw fw-success fw-2x text-info"></i>';

            } else if (resObj[i].status == "failed") {
                statusIcon = '<i class="fw fw-block fw-2x text-danger"></i>';
            } else {
                statusIcon = '<i class="fw fw-loader5 fw-2x text-warning"></i>';

            }
            tableHtml = tableHtml + '<tr><td>' + resObj[i].remoteUrl + '</td>' +
                        '<td>' + statusIcon + '</td>' +
                        '<td>' + '<a href=""><i class="fw fw-checklist fw-2x"></i></a>' + '</td>' +
                        '<td>' + '<a href=""><i class="fw fw-checklist fw-2x"></i></a>' +
                        '<a href=""><i class="fw fw-checklist fw-2x"></i></a>' +
                        '<a href=""><i class="fw fw-checklist fw-2x"></i></a>' +
                        '<a href=""><i class="fw fw-checklist fw-2x"></i></a>' +

                        '</td></tr>';
        }
        $('#customImagesTable').html(tableHtml);

    }, function (jqXHR, textStatus, errorThrown) {

    });
}
