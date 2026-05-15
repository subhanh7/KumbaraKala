package com.kumbarakala.app.data

import io.github.jan.supabase.createSupabaseClient

import io.github.jan.supabase.gotrue.Auth

import io.github.jan.supabase.gotrue.auth

import io.github.jan.supabase.postgrest.Postgrest

val supabase = createSupabaseClient(
    supabaseUrl = "https://zptukvhzjswnfzcypvdl.supabase.co/",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InpwdHVrdmh6anN3bmZ6Y3lwdmRsIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Nzg2NDIzMTQsImV4cCI6MjA5NDIxODMxNH0.eNc8I1HhZW2VKOHzcZyLjRBL5abnwgNMHowJoXOh03Y"
) {

    install(Auth) {

        // Keeps session refreshed automatically

        alwaysAutoRefresh = true

        // Loads saved session when app opens

        autoLoadFromStorage = true

        // Saves session locally after login

        autoSaveToStorage = true

    }

    install(Postgrest)

}