
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);


import CallCallManager from "./components/listers/CallCallCards"
import CallCallDetail from "./components/listers/CallCallDetail"

import DriverDriverManager from "./components/listers/DriverDriverCards"
import DriverDriverDetail from "./components/listers/DriverDriverDetail"

import PaymentPaymentManager from "./components/listers/PaymentPaymentCards"
import PaymentPaymentDetail from "./components/listers/PaymentPaymentDetail"

import NotificationLogManager from "./components/listers/NotificationLogCards"
import NotificationLogDetail from "./components/listers/NotificationLogDetail"


export default new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [
            {
                path: '/calls/calls',
                name: 'CallCallManager',
                component: CallCallManager
            },
            {
                path: '/calls/calls/:id',
                name: 'CallCallDetail',
                component: CallCallDetail
            },

            {
                path: '/drivers/drivers',
                name: 'DriverDriverManager',
                component: DriverDriverManager
            },
            {
                path: '/drivers/drivers/:id',
                name: 'DriverDriverDetail',
                component: DriverDriverDetail
            },

            {
                path: '/payments/payments',
                name: 'PaymentPaymentManager',
                component: PaymentPaymentManager
            },
            {
                path: '/payments/payments/:id',
                name: 'PaymentPaymentDetail',
                component: PaymentPaymentDetail
            },

            {
                path: '/notifications/logs',
                name: 'NotificationLogManager',
                component: NotificationLogManager
            },
            {
                path: '/notifications/logs/:id',
                name: 'NotificationLogDetail',
                component: NotificationLogDetail
            },



    ]
})
