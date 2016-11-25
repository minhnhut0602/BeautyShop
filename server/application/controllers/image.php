<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Image extends CI_Controller {

    /**
     * Index Page for this controller.
     *
     * Maps to the following URL
     * 		http://example.com/index.php/welcome
     *	- or -
     * 		http://example.com/index.php/welcome/index
     *	- or -
     * Since this controller is set as the default controller in
     * config/routes.php, it's displayed at http://example.com/
     *
     * So any other public methods not prefixed with an underscore will
     * map to /index.php/welcome/<method_name>
     * @see http://codeigniter.com/user_guide/general/urls.html
     */

    function __construct() {
        // Call the Model constructor
        parent::__construct ();

        if(true) { // testing
            error_reporting(E_ALL ^ (E_NOTICE | E_WARNING));
        }

        $this->load->database ();
        $this->load->model ( 'image_model' );
        $this->load->helper ( 'url' );
        $this->load->library('util');
        date_default_timezone_set('Asia/Seoul');
    }

    function index()
    {
        $pageNum = $this->input->get('page_num');

        if($pageNum == null) {
            $pageNum = 1;
        }

        $select_image_id = $this->input->get('image_id');
        $select_image_index = $this->input->get('image_index');

        $data = Array();
        $arrImage = $this->image_model->getLocalImageList($pageNum);
        $data['arrImage'] = $arrImage;
        $data['totalPageCnt'] = ((int)($this->image_model->getLocalImageCount() /(ADMIN_IMAGE_ROW_CNT * ADMIN_IMAGE_COL_CNT))) + 1;
        $data['currentPageNum'] = $pageNum;

        if($select_image_id == null) {
            $select_image= $arrImage[0];
            $select_image_index = 0;
        }
        else {
            $select_image = $this->image_model->getImageByID($select_image_id);
        }

        //load the view
        $data['main_content'] = 'admin/image';
        $data['selectImage'] = $select_image;
        $data['selectImageIdx'] = $select_image_index;
        $data['tab_id'] = ADMIN_TAB_ID_MONITOR;
        $this->load->view('includes/template', $data);
    }


    function  select_image () {
        $select_image_id = $this->input->post('image_id');
        $select_image_index = $this->input->post('image_index');

        if($select_image_id == null || $select_image_index == null) {
            Util::doRespond($this, -1, MSG_FAIL, null);
            return;
        }

        $select_image = $this->image_model->getImageByID($select_image_id);
        $data['image_info'] = $select_image;
        $data['image_index'] = $select_image_index;
        Util::doRespond($this, 0, MSG_SUCCESS, $data);
    }

    function  remove_image() {
        $image_id = $this->input->post('image_id');

        if($image_id == null) {
            Util::doRespond($this, -1, MSG_FAIL, null);
            return;
        }

        $this->image_model->removeImage($image_id);
        Util::doRespond($this, 0, MSG_SUCCESS, null);
    }
}

/* End of file welcome.php */
/* Location: ./application/controllers/welcome.php */