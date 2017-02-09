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

    $(document).on('click','.deleteImage',function(e){
        e.preventDefault();
        var imageId = $(this).closest('tr').data('uid');
        console.log(imageId);
        deleteImage(imageId);

    });
    $(document).on('click','.updateImage',function(e){
        e.preventDefault();
        var imageId = $(this).closest('tr').data('uid');
        updateImage(imageId);
    });
    $(document).on('click', '.viewResult', function (e) {
        e.preventDefault();
        var resultJson = $(this).data('uid');
        resultJson = decodeURI(resultJson);
        //resultJson = JSON.parse(resultJson);

        $('#viewResultModal .modal-body').val(resultJson);
       // $('#viewResultModal').show();
    });

    //$(".deleteImage").click(function (event) {
        //alert("hi");
        //deleteImage("mc.com-rayyildizhelloworldgoweblatest");
    //});
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
            if (result == "false") { // isImageAvailable=false means image is not added currently.
                jagg.post("../blocks/customDockerImages/ajax/customDockerImages.jag", {
                    action: "addImageAndCheckSecurity",
                    imageUrl: $("#imageUrl").val().trim()
                }, function (result) {
                    fillImagesListTable();
                    jagg.message({
                                     content: 'New Image added and queued for security check. It will take some time!',
                                     type: 'info',
                                     id: 'addnewcustomdockerimage'
                                 });
                    $("#addImage").loadingButton({action: 'hide'}).prop("disabled", true);
                    $('#imageUrl').prop("disabled", false).val("");

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
        var pendingImagesAvailable = false;
        jagg.post("../blocks/customDockerImages/ajax/customDockerImages.jag", {
            action: "getAllImages",
            imageUrl: $("#imageUrl").val().trim()
        }, function (result) {
            var tableHtml = '<tr><th>Image Url</th><th>Status</th><th>Test Report</th><th>Options</th><th>Last Updated</th></tr>';
            var resObj = JSON.parse(result);
            if (resObj.length > 0) { // panel will only be shown if there are 1 or more images
                $('#dockerImagesPanelDiv').show();
            } else {
                $('#dockerImagesPanelDiv').hide();

            }
            for (i = 0; i < resObj.length; i++) {
                var statusIcon, notActiveCreateApplication = "", notActiveModifyImage = "";
                if (resObj[i].status == "passed") {
                    statusIcon = '<i class="fw fw-success fw-2x text-info"></i>';
                } else if (resObj[i].status == "failed") {
                    statusIcon = '<i class="fw fw-block fw-2x text-danger"></i>';
                    notActiveCreateApplication = "not-active";
                } else {
                    statusIcon = '<i class="fw fw-loader5 fw-spin fw-2x text-warning"></i>';
                    notActiveCreateApplication = "not-active";
                    notActiveModifyImage = "not-active";
                    pendingImagesAvailable = true;

                }
                tableHtml += '<tr data-uid="'+ resObj[i].imageId +'"><td>' + resObj[i].remoteUrl + '</td>' +
                             '<td>' + statusIcon + '</td>' +
                             '<td>' + '<a href="#viewResultModal" data-uid="' + encodeURI(resObj[i].results) + '" data-toggle="modal" class="' + notActiveModifyImage + ' viewResult" title="View test report"><i class="fw fw-checklist fw-2x"></i></a>' + '</td>' +
                             '<td>' +
                             '<a href="application.jag?appTypeName=custom&selectedImageId=bla" class="btn ' + notActiveCreateApplication + '" title="Create application using this image" ><i class="fw fw-application fw-2x"></i></a>' +
                             '<a href="" class="btn ' + notActiveModifyImage + ' updateImage" title="Update image"><i class="fw fw-upload fw-2x "></i></a>' +
                             '<a href="" class="btn ' + notActiveModifyImage + ' deleteImage" title="Delete image"><i class="fw fw-delete fw-2x "></i></a>' +
                             '<td>' + resObj[i].lastUpdated + '</td>' +
                             '</td></tr>';
            }
            $('#customImagesTable').html(tableHtml);
            if(pendingImagesAvailable) {
                setTimeout(fillImagesListTable,5000); // this will poll while pendig images are available
            }

        }, function (jqXHR, textStatus, errorThrown) {

        });


    }

    function deleteImage(imageId) {
        jagg.post("../blocks/customDockerImages/ajax/customDockerImages.jag", {
            action: "deleteImage",
            imageId: imageId
        }, function (result) {
                if(result == "true") {
                    jagg.message({
                                     content: imageId + ' deleted successfully',
                                     type: 'success',
                                     id: 'deleteimage'
                                 });
                }
            fillImagesListTable();
        }, function (jqXHR, textStatus, errorThrown) {

        });

    }

    function updateImage(imageId) {
        jagg.post("../blocks/customDockerImages/ajax/customDockerImages.jag", {
            action: "updateImage",
            imageId: imageId
        }, function (result) {
            if(result == "true") {
                jagg.message({
                                 content: imageId + ' updated successfully',
                                 type: 'success',
                                 id: 'deleteimage'
                             });
            }
            fillImagesListTable();
        }, function (jqXHR, textStatus, errorThrown) {

        });
    }


