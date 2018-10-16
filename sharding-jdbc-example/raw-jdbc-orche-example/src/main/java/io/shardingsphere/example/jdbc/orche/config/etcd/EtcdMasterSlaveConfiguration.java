/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.shardingsphere.example.jdbc.orche.config.etcd;

import io.shardingsphere.api.config.MasterSlaveRuleConfiguration;
import io.shardingsphere.example.config.DataSourceUtil;
import io.shardingsphere.orchestration.config.OrchestrationConfiguration;
import io.shardingsphere.shardingjdbc.orchestration.api.OrchestrationMasterSlaveDataSourceFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/*
 * Please make sure master-slave data sync on MySQL is running correctly. Otherwise this example will query empty data from slave.
 */
public class EtcdMasterSlaveConfiguration extends EtcdExampleConfiguration {
    
    public EtcdMasterSlaveConfiguration(final boolean loadConfigFromRegCenter) {
        super(loadConfigFromRegCenter);
    }
    
    @Override
    protected DataSource getDataSourceFromRegCenter() throws SQLException {
        return OrchestrationMasterSlaveDataSourceFactory.createDataSource(new OrchestrationConfiguration("orchestration-master-slave-data-source", getRegistryCenterConfiguration(), false));
    }
    
    @Override
    protected DataSource getDataSourceFromLocalConfiguration() throws SQLException {
        MasterSlaveRuleConfiguration masterSlaveRuleConfig = new MasterSlaveRuleConfiguration("demo_ds_master_slave", "demo_ds_master", Arrays.asList("demo_ds_slave_0", "demo_ds_slave_1"));
        OrchestrationConfiguration orchestrationConfig = new OrchestrationConfiguration(
                "orchestration-master-slave-data-source", getRegistryCenterConfiguration(), true);
        return OrchestrationMasterSlaveDataSourceFactory.createDataSource(createDataSourceMap(), masterSlaveRuleConfig, new HashMap<String, Object>(), new Properties(), orchestrationConfig);
    }
    
    private Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>();
        result.put("demo_ds_master", DataSourceUtil.createDataSource("demo_ds_master"));
        result.put("demo_ds_slave_0", DataSourceUtil.createDataSource("demo_ds_slave_0"));
        result.put("demo_ds_slave_1", DataSourceUtil.createDataSource("demo_ds_slave_1"));
        return result;
    }
}
