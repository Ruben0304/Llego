# GraphQL Setup Instructions

## Overview
Apollo GraphQL has been integrated into the Llego Kotlin Multiplatform project to fetch store data from the backend API.

## Setup Status ✅
The following components have been configured:

1. **Dependencies Added**: Apollo GraphQL plugin and dependencies added to gradle files
2. **Schema Downloaded**: GraphQL schema saved to `/composeApp/src/commonMain/graphql/schema.graphqls`
3. **Query Created**: Store query created at `/composeApp/src/commonMain/graphql/GetStores.graphql`
4. **Client Configured**: Apollo client configured in `/composeApp/src/commonMain/kotlin/com/llego/multiplatform/data/network/GraphQLClient.kt`
5. **Repository Updated**: StoreRepository updated to use GraphQL (with fallback to mock data)

## Next Steps (To Complete Integration) 🚧

### 1. Fix Network Connectivity & Build Project
The project needs to be built to generate Apollo code. Currently there are network issues with Maven repositories.

```bash
# Try building the project
./gradlew :composeApp:build

# If network issues persist, try with offline mode
./gradlew --offline :composeApp:build
```

### 2. Enable Apollo Runtime Dependency
Once the build works, uncomment the Apollo runtime dependency in `/composeApp/build.gradle.kts`:

```kotlin
commonMain.dependencies {
    // Apollo GraphQL
    implementation(libs.apollo.runtime)  // Uncomment this line
}
```

### 3. Update StoreRepository Implementation
After successful build, Apollo will generate the `GetStoresQuery` class. Update the StoreRepository:

```kotlin
// In /composeApp/src/commonMain/kotlin/com/llego/multiplatform/data/repositories/StoreRepository.kt
// Replace the commented GraphQL code with:

suspend fun getStores(): List<Store> {
    return try {
        val response = apolloClient.query(GetStoresQuery()).execute()
        response.data?.stores?.map { storeData ->
            Store(
                id = storeData.id,
                name = storeData.name,
                etaMinutes = storeData.etaMinutes,
                logoUrl = storeData.logoUrl,
                bannerUrl = storeData.bannerUrl
            )
        } ?: emptyList()
    } catch (e: Exception) {
        // Fallback to mock data if GraphQL fails
        getMockStores()
    }
}
```

### 4. Import Generated Classes
Add the import for the generated GraphQL query:

```kotlin
// Add to imports in StoreRepository.kt
import com.llego.multiplatform.graphql.GetStoresQuery
```

## File Structure
```
composeApp/src/commonMain/
├── graphql/
│   ├── schema.graphqls              # GraphQL schema
│   └── GetStores.graphql           # Store query
└── kotlin/com/llego/multiplatform/
    └── data/
        ├── network/
        │   └── GraphQLClient.kt    # Apollo client configuration
        ├── model/
        │   └── Store.kt           # Store data model (already existed)
        └── repositories/
            └── StoreRepository.kt  # Updated with GraphQL support
```

## Backend API
- **Endpoint**: `https://llegobackend-production.up.railway.app/graphql`
- **Query**: The `GetStores` query fetches: id, name, etaMinutes, logoUrl, bannerUrl

## Testing
Once completed, the app will:
1. Attempt to fetch real store data from GraphQL API
2. Fall back to mock data if the API call fails
3. Display stores in the UI using the same interface as before

The ViewModel (`HomeViewModel`) is already compatible and will work seamlessly with the updated repository.