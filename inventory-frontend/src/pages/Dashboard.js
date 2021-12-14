import React, { Component } from 'react';
import { Result, Button } from 'antd';
import { SmileOutlined } from '@ant-design/icons';

class Dashboard extends Component {
    render() {
        return (
            <>
                <Result
                    icon={< SmileOutlined />}
                    title="Coming soon..."
                    extra={<Button Button type="primary" > Back</Button>}
                />
            </>
        );
    }
}

export default Dashboard;