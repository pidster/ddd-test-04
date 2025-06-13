# Frontend Development Instructions for Lakeside Mutual

## React/TypeScript Applications

### Project Structure

```
src/
├── components/           # Reusable UI components
│   ├── common/          # Generic components (Button, Input, etc.)
│   ├── layout/          # Layout components (Header, Sidebar, etc.)
│   └── domain/          # Domain-specific components
├── pages/               # Page-level components
│   ├── customer/        # Customer-related pages
│   ├── policy/          # Policy-related pages
│   └── claims/          # Claims-related pages
├── services/            # API integration services
│   ├── api/             # HTTP client configuration
│   └── {context}/       # Service per bounded context
├── store/               # Redux state management
│   ├── slices/          # Redux Toolkit slices
│   └── middleware/      # Custom middleware
├── hooks/               # Custom React hooks
├── types/               # TypeScript type definitions
├── utils/               # Utility functions
└── assets/              # Static assets
```

### TypeScript Patterns

#### Strict Type Definitions
```typescript
// Always use specific types, never 'any'
interface Customer {
  readonly id: string;
  readonly name: string;
  readonly email: string;
  readonly address: Address;
  readonly createdAt: Date;
  readonly updatedAt: Date;
}

interface Address {
  readonly street: string;
  readonly city: string;
  readonly zipCode: string;
  readonly country: string;
}

// Use discriminated unions for different states
type CustomerLoadingState = 
  | { status: 'idle' }
  | { status: 'loading' }
  | { status: 'succeeded'; data: Customer }
  | { status: 'failed'; error: string };
```

#### Domain Types
```typescript
// Use branded types for domain identifiers
type CustomerId = string & { readonly brand: unique symbol };
type PolicyId = string & { readonly brand: unique symbol };

// Factory functions for type safety
export const createCustomerId = (value: string): CustomerId => {
  if (!value || value.trim().length === 0) {
    throw new Error('Customer ID cannot be empty');
  }
  return value as CustomerId;
};

// Validation functions
export const isValidEmail = (email: string): boolean => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};
```

### React Patterns

#### Functional Components with Hooks
```typescript
interface CustomerDetailProps {
  customerId: CustomerId;
  onUpdate?: (customer: Customer) => void;
}

export const CustomerDetail: React.FC<CustomerDetailProps> = ({ 
  customerId, 
  onUpdate 
}) => {
  const [isEditing, setIsEditing] = useState(false);
  const { customer, loading, error } = useCustomer(customerId);
  
  const handleUpdate = useCallback(async (updatedCustomer: Customer) => {
    try {
      await updateCustomer(updatedCustomer);
      onUpdate?.(updatedCustomer);
      setIsEditing(false);
    } catch (error) {
      // Handle error appropriately
    }
  }, [onUpdate]);
  
  if (loading) return <LoadingSpinner />;
  if (error) return <ErrorMessage error={error} />;
  if (!customer) return <NotFound />;
  
  return (
    <CustomerCard 
      customer={customer}
      isEditing={isEditing}
      onEdit={() => setIsEditing(true)}
      onUpdate={handleUpdate}
      onCancel={() => setIsEditing(false)}
    />
  );
};
```

#### Custom Hooks
```typescript
// Domain-specific hooks
export const useCustomer = (customerId: CustomerId) => {
  const [state, setState] = useState<CustomerLoadingState>({ status: 'idle' });
  
  useEffect(() => {
    const fetchCustomer = async () => {
      setState({ status: 'loading' });
      try {
        const customer = await customerService.getById(customerId);
        setState({ status: 'succeeded', data: customer });
      } catch (error) {
        setState({ 
          status: 'failed', 
          error: error instanceof Error ? error.message : 'Unknown error' 
        });
      }
    };
    
    fetchCustomer();
  }, [customerId]);
  
  return {
    customer: state.status === 'succeeded' ? state.data : null,
    loading: state.status === 'loading',
    error: state.status === 'failed' ? state.error : null,
  };
};

// Form management hooks
export const useCustomerForm = (initialCustomer?: Customer) => {
  const [formData, setFormData] = useState<CustomerFormData>(() => 
    initialCustomer ? toFormData(initialCustomer) : getEmptyFormData()
  );
  const [errors, setErrors] = useState<ValidationErrors>({});
  
  const validate = useCallback((data: CustomerFormData): ValidationErrors => {
    const errors: ValidationErrors = {};
    
    if (!data.name.trim()) {
      errors.name = 'Name is required';
    }
    
    if (!isValidEmail(data.email)) {
      errors.email = 'Valid email is required';
    }
    
    return errors;
  }, []);
  
  const handleSubmit = useCallback(async (onSubmit: (data: CustomerFormData) => Promise<void>) => {
    const validationErrors = validate(formData);
    setErrors(validationErrors);
    
    if (Object.keys(validationErrors).length === 0) {
      await onSubmit(formData);
    }
  }, [formData, validate]);
  
  return {
    formData,
    setFormData,
    errors,
    handleSubmit,
    isValid: Object.keys(errors).length === 0,
  };
};
```

### Redux with Redux Toolkit

#### Store Configuration
```typescript
import { configureStore } from '@reduxjs/toolkit';
import { customerSlice } from './slices/customerSlice';
import { policySlice } from './slices/policySlice';
import { claimsSlice } from './slices/claimsSlice';

export const store = configureStore({
  reducer: {
    customer: customerSlice.reducer,
    policy: policySlice.reducer,
    claims: claimsSlice.reducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: ['persist/PERSIST'],
      },
    }),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
```

#### Slice Definitions
```typescript
import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';

// Async thunks
export const fetchCustomer = createAsyncThunk(
  'customer/fetchById',
  async (customerId: CustomerId, { rejectWithValue }) => {
    try {
      return await customerService.getById(customerId);
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : 'Unknown error');
    }
  }
);

export const updateCustomer = createAsyncThunk(
  'customer/update',
  async (customer: Customer, { rejectWithValue }) => {
    try {
      return await customerService.update(customer);
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : 'Unknown error');
    }
  }
);

// Slice
interface CustomerState {
  customers: Record<string, Customer>;
  loading: Record<string, boolean>;
  errors: Record<string, string | null>;
}

const initialState: CustomerState = {
  customers: {},
  loading: {},
  errors: {},
};

export const customerSlice = createSlice({
  name: 'customer',
  initialState,
  reducers: {
    clearError: (state, action: PayloadAction<string>) => {
      delete state.errors[action.payload];
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchCustomer.pending, (state, action) => {
        state.loading[action.meta.arg] = true;
        delete state.errors[action.meta.arg];
      })
      .addCase(fetchCustomer.fulfilled, (state, action) => {
        state.customers[action.payload.id] = action.payload;
        state.loading[action.meta.arg] = false;
      })
      .addCase(fetchCustomer.rejected, (state, action) => {
        state.loading[action.meta.arg] = false;
        state.errors[action.meta.arg] = action.payload as string;
      });
  },
});
```

#### Typed Hooks
```typescript
import { useDispatch, useSelector, TypedUseSelectorHook } from 'react-redux';

export const useAppDispatch = () => useDispatch<AppDispatch>();
export const useAppSelector: TypedUseSelectorHook<RootState> = useSelector;

// Selector functions
export const selectCustomerById = (customerId: CustomerId) => 
  (state: RootState): Customer | undefined => 
    state.customer.customers[customerId];

export const selectCustomerLoading = (customerId: CustomerId) =>
  (state: RootState): boolean =>
    state.customer.loading[customerId] ?? false;
```

### Material UI Patterns

#### Theme Configuration
```typescript
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { CssBaseline } from '@mui/material';

const theme = createTheme({
  palette: {
    mode: 'light',
    primary: {
      main: '#1976d2', // Lakeside Mutual brand color
    },
    secondary: {
      main: '#dc004e',
    },
  },
  typography: {
    fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
    h1: {
      fontSize: '2rem',
      fontWeight: 500,
    },
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          textTransform: 'none', // Disable uppercase transformation
        },
      },
    },
  },
});

export const App: React.FC = () => (
  <ThemeProvider theme={theme}>
    <CssBaseline />
    {/* Your app components */}
  </ThemeProvider>
);
```

#### Styled Components
```typescript
import { styled } from '@mui/material/styles';
import { Card, CardContent } from '@mui/material';

export const CustomerCard = styled(Card)(({ theme }) => ({
  marginBottom: theme.spacing(2),
  '&:hover': {
    boxShadow: theme.shadows[4],
  },
}));

export const CustomerCardContent = styled(CardContent)(({ theme }) => ({
  '&:last-child': {
    paddingBottom: theme.spacing(2),
  },
}));
```

### Service Layer

#### API Client Configuration
```typescript
import axios, { AxiosInstance, AxiosResponse } from 'axios';

class ApiClient {
  private client: AxiosInstance;
  
  constructor(baseURL: string) {
    this.client = axios.create({
      baseURL,
      timeout: 10000,
      headers: {
        'Content-Type': 'application/json',
      },
    });
    
    this.setupInterceptors();
  }
  
  private setupInterceptors() {
    this.client.interceptors.request.use(
      (config) => {
        // Add auth token if available
        const token = localStorage.getItem('authToken');
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => Promise.reject(error)
    );
    
    this.client.interceptors.response.use(
      (response) => response,
      (error) => {
        // Handle common errors
        if (error.response?.status === 401) {
          // Redirect to login
        }
        return Promise.reject(error);
      }
    );
  }
  
  async get<T>(url: string): Promise<T> {
    const response: AxiosResponse<T> = await this.client.get(url);
    return response.data;
  }
  
  async post<T>(url: string, data: unknown): Promise<T> {
    const response: AxiosResponse<T> = await this.client.post(url, data);
    return response.data;
  }
}

export const apiClient = new ApiClient(process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080');
```

#### Domain Services
```typescript
export class CustomerService {
  private readonly basePath = '/api/v1/customers';
  
  async getById(id: CustomerId): Promise<Customer> {
    return apiClient.get<Customer>(`${this.basePath}/${id}`);
  }
  
  async create(customerData: CreateCustomerRequest): Promise<Customer> {
    return apiClient.post<Customer>(this.basePath, customerData);
  }
  
  async update(customer: Customer): Promise<Customer> {
    return apiClient.put<Customer>(`${this.basePath}/${customer.id}`, customer);
  }
  
  async search(criteria: CustomerSearchCriteria): Promise<CustomerSearchResult> {
    const params = new URLSearchParams(criteria as Record<string, string>);
    return apiClient.get<CustomerSearchResult>(`${this.basePath}/search?${params}`);
  }
}

export const customerService = new CustomerService();
```

### Error Handling

#### Error Boundaries
```typescript
interface ErrorBoundaryState {
  hasError: boolean;
  error?: Error;
}

export class ErrorBoundary extends React.Component<
  React.PropsWithChildren<{}>,
  ErrorBoundaryState
> {
  constructor(props: React.PropsWithChildren<{}>) {
    super(props);
    this.state = { hasError: false };
  }
  
  static getDerivedStateFromError(error: Error): ErrorBoundaryState {
    return { hasError: true, error };
  }
  
  componentDidCatch(error: Error, errorInfo: React.ErrorInfo) {
    console.error('Error caught by boundary:', error, errorInfo);
    // Log to error reporting service
  }
  
  render() {
    if (this.state.hasError) {
      return (
        <Box textAlign="center" p={4}>
          <Typography variant="h4" color="error" gutterBottom>
            Something went wrong
          </Typography>
          <Typography variant="body1" color="textSecondary">
            We apologize for the inconvenience. Please try refreshing the page.
          </Typography>
          <Button 
            variant="contained" 
            onClick={() => window.location.reload()}
            sx={{ mt: 2 }}
          >
            Refresh Page
          </Button>
        </Box>
      );
    }
    
    return this.props.children;
  }
}
```

### Testing Patterns

#### Component Testing
```typescript
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { CustomerDetail } from './CustomerDetail';

const mockStore = configureStore({
  reducer: {
    customer: customerSlice.reducer,
  },
  preloadedState: {
    customer: {
      customers: {
        'customer-1': mockCustomer,
      },
      loading: {},
      errors: {},
    },
  },
});

const renderWithProviders = (component: React.ReactElement) => {
  return render(
    <Provider store={mockStore}>
      <ThemeProvider theme={theme}>
        {component}
      </ThemeProvider>
    </Provider>
  );
};

describe('CustomerDetail', () => {
  it('should display customer information', () => {
    renderWithProviders(
      <CustomerDetail customerId={createCustomerId('customer-1')} />
    );
    
    expect(screen.getByText('John Doe')).toBeInTheDocument();
    expect(screen.getByText('john@example.com')).toBeInTheDocument();
  });
  
  it('should handle update action', async () => {
    const onUpdate = jest.fn();
    
    renderWithProviders(
      <CustomerDetail 
        customerId={createCustomerId('customer-1')} 
        onUpdate={onUpdate}
      />
    );
    
    fireEvent.click(screen.getByText('Edit'));
    fireEvent.change(screen.getByLabelText('Name'), {
      target: { value: 'Jane Doe' },
    });
    fireEvent.click(screen.getByText('Save'));
    
    await waitFor(() => {
      expect(onUpdate).toHaveBeenCalled();
    });
  });
});
```

### Performance Optimization

- Use `React.memo` for expensive components
- Implement proper dependency arrays in `useEffect` and `useCallback`
- Use `useMemo` for expensive calculations
- Implement virtualization for large lists
- Lazy load routes and components
- Optimize bundle size with code splitting

### Accessibility

- Use semantic HTML elements
- Implement proper ARIA attributes
- Ensure keyboard navigation
- Maintain color contrast ratios
- Provide alternative text for images
- Test with screen readers

Remember: Prioritize user experience and maintain consistency with the design system.
