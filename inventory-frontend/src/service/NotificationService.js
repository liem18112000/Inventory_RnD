import axios from 'axios'
import { baseNotificationAPI } from '../constant'
import { getHeaderByGatewayStatus } from "../core/utility/GatewayHeaderConfig";
import { FilterRequestMapper } from "../core/models/mapper/ModelMapper";
import {
    mockGetPageNotification,
    mockGetPageEvent,
    mockGetEventById,
    mockGetNotificationById
} from "../core/models/MockDataModel";
import {addActorNameAndRole} from "../core/utility/RequestActorConfig";

// Notification base URL
const BaseURL = baseNotificationAPI()

/**
 * Notification service
 */
export class NotificationService {

    /**
     * Default constructor
     */
    constructor() {
        this.mapper = new FilterRequestMapper();
    }

    /**
     * Get page of notification event by filter
     * @param filter        Filter on name, description and occur at
     * @param page          Page
     * @param rows          Size per page
     * @param sortField     Sorting field (default field is id)
     * @param sortOrder     Sorting order (default order is desc)
     * @param isMock        Activate mock if true otherwise use real api call
     */
    getPageEvent(filter, page, rows, sortField, sortOrder, isMock = true) {
        if (isMock) {
            return mockGetPageEvent();
        }

        const url = `${BaseURL}/event/page`;
        const body = this.mapper.toRequest(filter, page, rows, sortField, sortOrder);
        const config = {
            data: JSON.parse(body),
            headers: getHeaderByGatewayStatus()
        };

        // Fetch notification event data from api
        return axios.get(url, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Get page of notification by filter
     * @param filter        Filter on name, description, type and status
     * @param page          Page
     * @param rows          Size per page
     * @param sortField     Sorting field (default field is id)
     * @param sortOrder     Sorting order (default order is desc)
     * @param isMock        Activate mock if true otherwise use real api call
     */
    getPageNotification(filter, page, rows, sortField, sortOrder, isMock = true) {
        if (isMock) {
            return mockGetPageNotification();
        }

        const url = `${BaseURL}/notification/page`;
        const body = this.mapper.toRequest(filter, page, rows, sortField, sortOrder);
        const config = {
            data: JSON.parse(body),
            headers: getHeaderByGatewayStatus()
        };

        // Fetch notification event data from api
        return axios.get(url, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Get event by id
     * @param id            event Id
     * @param isMock        Activate mock if true otherwise use real api call
     * @returns {Promise<AxiosResponse<any> | void>|Promise<{occurAt: string, name: string, accessAt: string, description: string, active: boolean, id: number, eventType: string}>}
     */
    getEventById(id, isMock = true) {
        if (isMock) {
            return mockGetEventById();
        }

        const url = `${BaseURL}/event/${id}`
        const config = { headers: getHeaderByGatewayStatus() };

        return axios
            .get(url, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }

    /**
     * Get notification by id
     * @param id            Notification ID
     * @param isMock        Activate mock if true otherwise use real api call
     * @returns {Promise<{notifyAt: string, name: string, accessAt: string, description: string, active: boolean, id: number, event: {occurAt: string, name: string, accessAt: string, description: string, active: boolean, id: number, eventType: string}, message: {subject: string, from: string, sendAt: string, to: string, body: string}, type: string, status: string}>|Promise<AxiosResponse<any> | void>}
     */
    getNotificationById(id, isMock = true) {
        if (isMock) {
            return mockGetNotificationById();
        }

        const url = `${BaseURL}/notification/${id}`
        const config = { headers: getHeaderByGatewayStatus() };

        return axios
            .get(url, config)
            .then(res => res.data)
            .catch(error => console.log(error));
    }
}