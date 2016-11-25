<table class="table table-bordered table-scrollable" id="tb_cast_list">
    <colgroup>
        <col width="70%">
        <col width="30%">
    </colgroup>

    <thead>
    <tr>
        <th scope="col" class="base-table-header" >일자</th>
        <th scope="col" class="base-table-header" >클릭수</th>
    </tr>
    </thead>

    <tbody id="list">
    <?php
    $cnt = count($arrBannerClickInfo);

    if($cnt == 0) {
        echo '<tr class="no_data" style="">';
        echo '<td colspan="10">없습니다.</td>';
        echo '</tr>';
    }
    else {
        $i = 1;
        foreach ($arrBannerClickInfo as $row) {
            echo '<tr>';
            echo '<td>' . $row['bannerclickTime'] . '</td>';
            echo '<td>' . $row['bannerclickCnt'] . '</td>';
        }
    }
    ?>
    </tbody>
</table>