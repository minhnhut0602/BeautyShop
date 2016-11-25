	<div id="footer">
		<hr>
		<div class="inner">
			<div class="container">
				<p class="right"><a href="<?php echo base_url(); ?>admin">기본 페이지</a></p>
				<p>
				</p>
			</div>
		</div>
	</div>

    <dialog id="confirmBox" style="width: 320px;">
        <form method="dialog">
            <section>
                <div class="message" id="message"></div>
            </section>

            <div  style="display: flex;justify-content: center;">
                <button class="yes" id="ok" type="reset">확인</button>
                <button  class="no" id="cancel" type="button" style="margin-left: 5px;">취소</button>
            </div>
        </form>
    </dialog>

    <script>
        function doConfirm(msg, yesFn, noFn)
        {
            var confirmBox = document.getElementById('confirmBox');
            var cancelButton = document.getElementById('cancel');
            var okButton = document.getElementById('ok');
            var message = document.getElementById('message');
            message.textContent=msg;

            // Form cancel button closes the dialog box
            cancelButton.addEventListener('click', function() {
                confirmBox.close();
            });

            okButton.addEventListener('click',yesFn);

            confirmBox.showModal();
        }
    </script>
</body>
</html>