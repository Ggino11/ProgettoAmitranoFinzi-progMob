package com.amitranofinzi.vimata.data.repository

import com.amitranofinzi.vimata.data.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryTest {

    private lateinit var authRepository: AuthRepository
    private val firebaseAuth = mock(FirebaseAuth::class.java)
    private val firestore = mock(FirebaseFirestore::class.java)
    private val firebaseUser = mock(FirebaseUser::class.java)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        authRepository = AuthRepository()
        `when`(firebaseAuth.currentUser).thenReturn(firebaseUser)
        `when`(firebaseUser.uid).thenReturn("testUserId")
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test checkEmailExists`() = runTest {
        val mockCollection = mock(CollectionReference::class.java)
        val mockQuerySnapshot = mock(QuerySnapshot::class.java)
        val mockTask = mock(Task::class.java) as Task<QuerySnapshot>

        `when`(firestore.collection("users")).thenReturn(mockCollection)
        `when`(mockCollection.whereEqualTo("email", "test@example.com")).thenReturn(mockCollection)
        `when`(mockCollection.get()).thenReturn(mockTask)
        `when`(mockTask.await()).thenReturn(mockQuerySnapshot)
        `when`(mockQuerySnapshot.isEmpty).thenReturn(false)

        val result = authRepository.checkEmailExists("test@example.com")
        assertTrue(result)
    }

    @Test
    fun `test register`() = runTest {
        val mockAuthResult = mock(AuthResult::class.java)
        val mockTask = mock(Task::class.java) as Task<AuthResult>
        val mockVoidTask = mock(Task::class.java) as Task<Void>

        `when`(firebaseAuth.createUserWithEmailAndPassword(anyString(), anyString())).thenReturn(mockTask)
        `when`(mockTask.await()).thenReturn(mockAuthResult)
        `when`(mockAuthResult.user).thenReturn(firebaseUser)
        `when`(firestore.collection("users").document(anyString()).set(any(User::class.java))).thenReturn(mockVoidTask)
        `when`(mockVoidTask.await()).thenReturn(null)

        val result = authRepository.register("test@example.com", "password", "trainer", "Test", "User")
        assertTrue(result.isSuccess)
    }


    @Test
    fun `test signOut`() {
        authRepository.signOut()
        verify(firebaseAuth).signOut()
    }

    @Test
    fun `test getUser`() = runTest {
        val mockDocumentSnapshot = mock(DocumentSnapshot::class.java)
        val mockTask = mock(Task::class.java) as Task<DocumentSnapshot>
        val mockUser = User("testUserId", "Test", "User", "test@example.com", "trainer", "password")

        `when`(firestore.collection("users").document("testUserId").get()).thenReturn(mockTask)
        `when`(mockTask.await()).thenReturn(mockDocumentSnapshot)
        `when`(mockDocumentSnapshot.toObject(User::class.java)).thenReturn(mockUser)

        val result = authRepository.getUser("testUserId")
        assertEquals(mockUser, result)
    }

    @Test
    fun `test getUserType`() = runTest {
        val mockDocumentSnapshot = mock(DocumentSnapshot::class.java)
        val mockTask = mock(Task::class.java) as Task<DocumentSnapshot>

        `when`(firestore.collection("users").document("testUserId").get()).thenReturn(mockTask)
        `when`(mockTask.await()).thenReturn(mockDocumentSnapshot)
        `when`(mockDocumentSnapshot.getString("userType")).thenReturn("trainer")

        val result = authRepository.getUserType("testUserId")
        assertTrue(result.isSuccess)
        assertEquals("trainer", result.getOrNull())
    }
}
