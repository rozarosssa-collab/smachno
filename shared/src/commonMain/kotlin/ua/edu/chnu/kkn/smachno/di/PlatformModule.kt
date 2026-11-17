package ua.edu.chnu.kkn.smachno.di

import org.koin.core.module.Module

/** KSafe створюється по-різному на кожній платформі. */
expect val platformModule: Module
