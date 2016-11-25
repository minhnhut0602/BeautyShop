<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Naver_shop extends CI_Controller {

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
        $this->load->library('naver/location');
        $this->load->library('naver/shop');
        $this->load->helper ( 'url' );
        date_default_timezone_set('Asia/Seoul');
    }

    function index()
    {
        //load the view
        $data['main_content'] = 'admin/map/shopsearch';
        $data['tab_id'] = ADMIN_TAB_ID_NAVOR;
        $this->load->view('includes/template', $data);
    }

    function  writeShopList() {
        $result = $this->shop->writeShopArrayFromNavor($_POST['query'], $_POST['page_num'], $_POST['category']);
	$this->output->set_content_type('application/json')->set_output($result);
    }

    function  writeLocationList() {
        $result = $this->location->writeLocationFromNavor($_POST['query']);
        $this->output->set_content_type ($result);
    }
}

/* End of file welcome.php */
/* Location: ./application/controllers/welcome.php */