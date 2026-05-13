package com.bagmanov.kmpnotes.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.bagmanov.kmpnotes.notes_manager.data.db.DatabaseFactory
import com.bagmanov.kmpnotes.notes_manager.data.db.NotesDatabase
import com.bagmanov.kmpnotes.notes_manager.data.repository.NotesRepositoryImpl
import com.bagmanov.kmpnotes.notes_manager.domain.interactor.AddNoteInteractor
import com.bagmanov.kmpnotes.notes_manager.domain.interactor.DeleteNoteInteractor
import com.bagmanov.kmpnotes.notes_manager.domain.interactor.EditNoteInteractor
import com.bagmanov.kmpnotes.notes_manager.domain.interactor.GetAllNotesInteractor
import com.bagmanov.kmpnotes.notes_manager.domain.interactor.GetNoteInteractor
import com.bagmanov.kmpnotes.notes_manager.domain.interactor.SearchNotesInteractor
import com.bagmanov.kmpnotes.notes_manager.domain.interactor.SwitchPinnedStatusInteractor
import com.bagmanov.kmpnotes.notes_manager.domain.repository.NotesRepository
import com.bagmanov.kmpnotes.notes_manager.domain.useCase.AddNoteUseCase
import com.bagmanov.kmpnotes.notes_manager.domain.useCase.DeleteNoteUseCase
import com.bagmanov.kmpnotes.notes_manager.domain.useCase.EditNoteUseCase
import com.bagmanov.kmpnotes.notes_manager.domain.useCase.GetAllNotesUseCase
import com.bagmanov.kmpnotes.notes_manager.domain.useCase.GetNoteUseCase
import com.bagmanov.kmpnotes.notes_manager.domain.useCase.SearchNotesUseCase
import com.bagmanov.kmpnotes.notes_manager.domain.useCase.SwitchPinnedStatusUseCase
import com.bagmanov.kmpnotes.notes_manager.presentation.creation.CreateNoteViewModel
import com.bagmanov.kmpnotes.notes_manager.presentation.editing.EditNoteViewModel
import com.bagmanov.kmpnotes.notes_manager.presentation.notes.NotesViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    single { get<NotesDatabase>().noteDao() }

    singleOf(::NotesRepositoryImpl).bind<NotesRepository>()

    factoryOf(::AddNoteInteractor).bind<AddNoteUseCase>()
    factoryOf(::DeleteNoteInteractor).bind<DeleteNoteUseCase>()
    factoryOf(::EditNoteInteractor).bind<EditNoteUseCase>()
    factoryOf(::GetAllNotesInteractor).bind<GetAllNotesUseCase>()
    factoryOf(::GetNoteInteractor).bind<GetNoteUseCase>()
    factoryOf(::SearchNotesInteractor).bind<SearchNotesUseCase>()
    factoryOf(::SwitchPinnedStatusInteractor).bind<SwitchPinnedStatusUseCase>()



    viewModelOf(::NotesViewModel)
    viewModelOf(::CreateNoteViewModel)
    viewModelOf(::EditNoteViewModel)
}