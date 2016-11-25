<?php
/**
 * Created by PhpStorm.
 * User: KCJ
 * Date: 8/6/2015
 * Time: 4:56 PM
 */

class Image_upload {

    private static $instance;

    function __construct() {
        self::$instance =& $this;

        // Assign all the class objects that were instantiated by the
        // bootstrap file (CodeIgniter.php) to local class variables
        // so that CI can run as one big super object.
        foreach (is_loaded() as $var => $class)
        {
            $this->$var =& load_class($class);
        }

        $this->load =& load_class('Loader', 'core');

        $this->load->initialize();

        $this->load->library('image_lib');
        $this->load->library('util');
        $this->load->helper ( 'url' );
        date_default_timezone_set('Asia/Seoul');
    }

    function uploadImage($dir_name, $input_name, $width = 0, $height = 0) {
        $w_uploadConfig = array (
            'upload_path' => $_SERVER ["DOCUMENT_ROOT"] . "/images/".$dir_name."/",
            'upload_url' => base_url () ."/images/".$dir_name."/",
            'allowed_types' => "*",
            'max_width' => 5000,
            'max_height' => 5000,
            'overwrite' => TRUE,
            'max_size' => "10000KB",
            'file_name' => $dir_name . $this->util->getMilliTime() . "_" . $input_name
        );
        $this->load->library ( 'upload' );
        $this->upload->initialize ( $w_uploadConfig );

        if ($this->upload->do_upload ( $input_name )) {

            $config ['image_library'] = 'gd2';
            $config ['source_image'] = $w_uploadConfig ['upload_path'] . $this->upload->file_name;
            $config ['maintain_ratio'] = TRUE;

            list ( $w, $h ) = getimagesize ( $w_uploadConfig ['upload_path'] . $this->upload->file_name );

            $ratio = max ( $width / $w, $height / $h );
            $h = ceil ( $h * $ratio );
            $w = ceil ( $w * $ratio );

            $config ['width'] = $w;
            $config ['height'] = $h;

            $this->image_lib->initialize ( $config );
            if (! $this->image_lib->resize ()) {
                $error = $this->image_lib->display_errors ();
                $ret = "";
                echo  $error;
            } else {
                $ret = $w_uploadConfig ['upload_url'] . $this->upload->file_name;
            }

            return $ret;
        } else {
            $error = "실패하였습니다.\n" . $this->upload->display_errors () . "\n" . $this->upload->upload_path;
            echo  $error;
            return "";
        }

        return null;
    }
}