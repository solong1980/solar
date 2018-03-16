package com.lszyhb.basicclass;

import java.util.List;

/**
 * Created by kkk8199 on 3/6/18.
 */

public class ShowCommitBatch extends ShowAbtAuth {

        private List<ShowDevices> batchAdds;
        private List<ShowDevices> batchDels;
        public List<ShowDevices> getBatchAdds() {
            return batchAdds;
        }
        public void setBatchAdds(List<ShowDevices> batchAdds) {
            this.batchAdds = batchAdds;
        }
        public List<ShowDevices> getBatchDels() {
            return batchDels;
        }
        public void setBatchDels(List<ShowDevices> batchDels) {
            this.batchDels = batchDels;
        }

}
