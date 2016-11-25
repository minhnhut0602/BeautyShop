<div class="container">

    <div class="row">
        <div class="col-md-4">
            <div class="panel panel-default">
                <div class="panel-heading">점포</div>
                <div class="panel-body">
                    <div class="input-group">
                        <label for="totoalShopCnt" id="totalShopCnt">총 상점:  <?php echo $shopInfo['totalShopCnt'] ?></label>
                    </div>
                    <div class="input-group">
                        <label for="liveShopCnt" id="liveShopCnt">라이브 중인 상점:    <?php echo $shopInfo['totalLiveShopCnt'] ?></label>
                    </div>
                    <div class="input-group">
                        <label for="hairShopCnt" id="hairShopCnt">헤어샵 총:    <?php echo $shopInfo['totalHairShopCnt'] ?></label>
                        <label for="liveShopForHairShopCnt" id="liveShopForHairShopCnt">라이브:    <?php echo $shopInfo['totalLiveHairShopCnt'] ?></label>
                    </div>
                    <div class="input-group">
                        <label for="nailShopCnt" id="nailShopCnt">네일샵 총:    <?php echo $shopInfo['totalNailShopCnt'] ?></label>
                        <label for="liveShopForNailShopCnt" id="liveShopForNailShopCnt">라이브:    <?php echo $shopInfo['totalLiveNailShopCnt'] ?></label>
                    </div>
                    <div class="input-group">
                        <label for="soknunShopCnt" id="soknunShopCnt">속눈썹연장샵 총:    <?php echo $shopInfo['totalSoknunShopCnt'] ?></label>
                        <label for="liveShopForSoknunShopCnt" id="liveShopForSoknunShopCnt">라이브:    <?php echo $shopInfo['totalLiveSoknunShopCnt'] ?></label>
                    </div>
                    <div class="input-group">
                        <label for="waksingShopCnt" id="waksingShopCnt">왁싱샵 총:    <?php echo $shopInfo['totalWaksingShopCnt'] ?></label>
                        <label for="liveShopForWaksingShopCnt" id="liveShopForWaksingShopCnt">라이브:    <?php echo $shopInfo['totalLiveWaksingShopCnt'] ?></label>
                    </div>
                    <div class="input-group">
                        <label for="skinShopCnt" id="skinShopCnt">피부샵 총:    <?php echo $shopInfo['totalSkinShopCnt'] ?></label>
                        <label for="liveShopForSkinShopCnt" id="liveShopForSkinShopCnt">라이브:    <?php echo $shopInfo['totalLiveSkinShopCnt'] ?></label>
                    </div>
                    <div class="input-group">
                        <label for="massageShopCnt" id="massageShopCnt">마사지샵 총:    <?php echo $shopInfo['totalMassageShopCnt'] ?></label>
                        <label for="liveShopForMassageShopCnt" id="liveShopForMassageShopCnt">라이브:    <?php echo $shopInfo['totalLiveMassageShopCnt'] ?></label>
                    </div>
                    <div class="input-group">
                        <label for="banyongguShopCnt" id="banyongguShopCnt">반영구화장 총:    <?php echo $shopInfo['totalBanyongguShopCnt'] ?></label>
                        <label for="liveShopForBanyongguShopCnt" id="liveShopForBanyongguShopCnt">라이브:    <?php echo $shopInfo['totalLiveBanyongguShopCnt'] ?></label>
                    </div>
                    <div class="input-group">
                        <label for="tatuShopCnt" id="tatuShopCnt">타투 총:    <?php echo $shopInfo['totalTatuShopCnt'] ?></label>
                        <label for="liveShopForTatuShopCnt" id="liveShopForTatuShopCnt">라이브:    <?php echo $shopInfo['totalLiveTatuShopCnt'] ?></label>
                    </div>


                    <div class="input-group">
                        <label for="todayShopRequestCnt" id="todayShopRequestCnt">오늘 점포신청:  <?php echo $shopInfo['todayRequestShopCnt'] ?></label>
                    </div>

                    <div class="input-group" onclick="showRequestedShopPage()" style="cursor: pointer;">
                        <label for="todayShopRequestCnt" id="todayShopRequestCnt"  style="cursor: pointer;">신청중 리스트:    <?php echo $shopInfo['totalRequestShopCnt'] ?></label>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-4">
            <div class="panel panel-default"  onclick="showQuestionList()" style="cursor: pointer;">
                <div class="panel-heading">제휴문의</div>
                <div class="panel-body">
                    <div class="input-group">
                        <label for="todayQuestionCnt" id="todayQuestionCnt">오늘문의:   <?php echo $todayQuestionCnt ?></label>
                    </div>
                </div>
            </div>

            <div class="panel panel-default">
                <div class="panel-heading">토크</div>
                <div class="panel-body">
                    <div class="input-group">
                        <label for="totalTalkCnt" id="totalTalkCnt">총 토크:   <?php echo $talkInfo['totalTalkCnt'] ?></label>
                    </div>
                    <div class="input-group">
                        <label for="todayTalkCnt" id="todayTalkCnt">오늘 토크:  <?php echo $talkInfo['todayTalkCnt'] ?></label>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-4">
            <div class="panel panel-default"  onclick="showWrongInfoList()" style="cursor: pointer;">
                <div class="panel-heading">잘못된 정보</div>
                <div class="panel-body">
                    <div class="input-group">
                        <label for="todayWrongContentCnt" id="todayWrongContentCnt">오늘 잘못된 내용:  <?php echo $wrongInfoCnt ?></label>
                    </div>
                </div>
            </div>
			<div class="row-md-4">			
	            <div class="btn-group-vertical" role="group" aria-label="function">
	                <button type="button" class="btn btn-default"><a href="<?php echo base_url(); ?>index.php/admin/manage_main_page">첫페이지관리</a></button>
	                <button type="button" class="btn btn-default"><a href="<?php echo base_url(); ?>index.php/admin/set_point_page">포인트지급</a></button>
	                <button type="button" class="btn btn-default"><a href="<?php echo base_url(); ?>index.php/shop/shop_password_question_list">상점비밀번호 문의</a></button>
	            </div>			
	            <div class="btn-group-vertical" style="margin: 20px;" role="group" aria-label="function">
	                <button type="button" class="btn btn-default"><a href="<?php echo base_url(); ?>index.php/admin/agreement?type=0">개인정보약관</a></button>
	                <button type="button" class="btn btn-default"><a href="<?php echo base_url(); ?>index.php/admin/agreement?type=1">미미샵이용약관</a></button>
	                <button type="button" class="btn btn-default"><a href="<?php echo base_url(); ?>index.php/admin/agreement?type=2">위치기반이용약관</a></button>
	            </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-4">
            <div class="panel panel-default">
                <div class="panel-heading">회원</div>
                <div class="panel-body">
                    <div class="input-group">
                        <label for="totoalMemberCnt" id="totalMemberCnt">총회원:   <?php echo $memberInfo['totalMemberCnt'] ?></label>
                    </div>
                    <div class="input-group">
                        <label for="todayLoginMemberCnt" id="todayLoginMemberCnt">오늘가입한 회원: <?php echo $memberInfo['totalTodayMemberCnt'] ?></label>
                    </div>
                    <div class="input-group">
                        <label for="totalManCnt" id="totalManCnt">총남자:  <?php echo $memberInfo['totalManMemberCnt'] ?></label>
                    </div>
                    <div class="input-group">
                        <label for="totalWomanCnt" id="totalWomanCnt">총여자:  <?php echo $memberInfo['totalWomanMemberCnt'] ?></label>
                    </div>
                    <div class="input-group">
                        <label for="totalAccessCnt" id="totalAccessCnt">방문자총수:  <?php echo $memberInfo['totalAccessCnt'] ?></label>
                    </div>
        
                </div>
            </div>

            <div class="input-group">
                <label for="total10Cnt" id="total10Cnt">10대:    <?php echo $memberInfo['total10MemberCnt'] ?></label>
            </div>

            <div class="input-group">
                <label for="total20Cnt" id="total20Cnt">20대:    <?php echo $memberInfo['total20MemberCnt'] ?></label>
            </div>

            <div class="input-group">
                <label for="total30Cnt" id="total30Cnt">30대:    <?php echo $memberInfo['total30MemberCnt'] ?></label>
            </div>

            <div class="input-group">
                <label for="total40Cnt" id="total40Cnt">40대:    <?php echo $memberInfo['total40MemberCnt'] ?></label>
            </div>

            <div class="input-group">
                <label for="total50Cnt" id="total50Cnt">50대:    <?php echo $memberInfo['total50MemberCnt'] ?></label>
            </div>
        </div>

        <div class="col-md-4">
            <div class="panel panel-default">
                <div class="panel-heading" onclick="showLowRemarkCommentPage()" style="cursor: pointer;">별점 낮은 댓글</div>
                <div class="panel-body">
                    <div class="bodycontainer scrollable">
                        <table class="table table-bordered table-scrollable">
                            <colgroup>
                                <col width="10%">
                                <col width="10%">
                                <col width="20%">
                                <col width="60%">
                            </colgroup>
                            <thead>
                            <tr>
                                <th scope="col">번호</th>
                                <th scope="col">점수</th>
                                <th scope="col">유저</th>
                                <th scope="col">내용</th>
                            </tr>
                            </thead>

                            <tbody id="list">
                                <?php
                                $cnt = count($arrLowHeartComment);

                                if($cnt == 0) {
                                    echo '<tr class="__oldlist" style="">';
                                    echo '<td colspan="9">없습니다.</td>';
                                    echo '</tr>';
                                }
                                else {
                                    foreach ($arrLowHeartComment as $row) {
                                        echo '<tr>';
                                        echo '<td>' . $row['id'] . '</td>';
                                        echo '<td>' . $row['shopcommentShopLevel'] . '</td>';
                                        echo '<td>' . $row['userID'] . '</td>';
                                        echo '<td>' . $row['shopcommentContent'] . '</td>';
                                        echo '</tr>';
                                    }
                                }
                                ?>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading" onclick="showDeclareCommentPage()"  style="cursor: pointer;">신고 들어온 댓글</div>
                <div class="panel-body" >
                    <div class="bodycontainer scrollable">
                        <table class="table table-bordered table-scrollable">
                            <colgroup>
                                <col width="10%">
                                <col width="70%">
                                <col width="20%">
                            </colgroup>
                            <thead>
                            <tr>
                                <th scope="col">번호</th>
                                <th scope="col">댓글내용</th>
                                <th scope="col">신고자</th>
                            </tr>
                            </thead>

                            <tbody id="list">
                            <?php
                            $cnt = count($arrDeclareComment);

                            if($cnt == 0) {
                                echo '<tr class="__oldlist" style="">';
                                echo '<td colspan="9">없습니다.</td>';
                                echo '</tr>';
                            }
                            else {
                                foreach ($arrDeclareComment as $row) {
                                    echo '<tr>';
                                    echo '<td>' . $row['id'] . '</td>';
                                    echo '<td>' . $row['freetalkcommentContent'] . '</td>';
                                    echo '<td>' . $row['declareUserID'] . '</td>';
                                    echo '</tr>';
                                }
                            }
                            ?>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-4">
            <div class="panel panel-default">
                <div class="panel-heading">이벤트기간 7일이하 상점</div>
                <div class="panel-body">
                    <div class="bodycontainer scrollable">
                        <table class="table table-bordered table-scrollable">
                            <colgroup>
                                <col width="10%">
                                <col width="60%">
                                <col width="30%">
                            </colgroup>
                            <thead>
                            <tr>
                                <th scope="col">번호</th>
                                <th scope="col">상점이름</th>
                                <th scope="col">남은일수</th>
                            </tr>
                            </thead>

                            <tbody id="list">
                            <?php
                            $cnt = count($arrLiveEvent);

                            if($cnt == 0) {
                                echo '<tr class="__oldlist" style="">';
                                echo '<td colspan="9">없습니다.</td>';
                                echo '</tr>';
                            }
                            else {
                                foreach ($arrLiveEvent as $row) {
                                    echo '<tr>';
                                    echo '<td>' . $row['id'] . '</td>';
                                    echo '<td>' . $row['shopName'] . '</td>';
                                    echo '<td>' . $row['eventRemainDate']. '</td>';
                                    echo '</tr>';
                                }
                            }
                            ?>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>


            <div class="input-group">
                <label for="totalDownloadCnt" id="totalDownloadCnt">총 어플 다운로드수: <?php echo $downloadInfo['totalDownloadCnt']?></label>
            </div>

            <div class="input-group">
                <label for="todayDownloadCnt" id="todayDownloadCnt">오늘 다운 수:    <?php echo $downloadInfo['todayDownloadCnt']?></label>
            </div>

            <div class="input-group">
                <label for="todayRemoveCnt" id="todayRemoveCnt">오늘 삭제 수:    <?php echo $downloadInfo['todayRemoveCnt']?></label>
            </div>

            <div class="input-group">
                <label for="totalRemoveCnt" id="totalRemoveCnt">총 삭제수:  <?php echo $downloadInfo['totalRemoveCnt']?></label>
            </div>

            <div class="input-group">
                <label for="totalLiveCnt" id="totalLiveCnt">총 라이브 수:    <?php echo $downloadInfo['totalLiveCnt']?></label>
            </div>
        </div>
    </div>

</div>


<script>
    function showLowRemarkCommentPage() {
        location.href = getBaseURL()+'/admin/low_remark_comment_page?page_num=1';
    }

    function showDeclareCommentPage() {
        location.href = getBaseURL()+'/freetalk/manage_comment?page_num=1&type=4';
    }

    function showRequestedShopPage() {
        location.href = getBaseURL()+'/shop/request_shop_list?page_num=1';
    }

    function showQuestionList() {
        location.href = getBaseURL()+'/admin/question_list?page_num=1';
    }

    function showWrongInfoList() {
        location.href =  getBaseURL()+'/shop/shop_wrong_info_list?page_num=1';
    }
</script>