# Lakeside Mutual Customer Portal

A modern React-based customer portal for Lakeside Mutual Insurance Company, built as part of a Domain-Driven Design (DDD) microservices architecture.

## 🏗️ Architecture

This customer portal is part of the Lakeside Mutual DDD system and implements:

- **Clean Architecture** with clear separation of concerns
- **Domain-Driven Design** principles aligned with business contexts
- **Responsive Design** using Material-UI components
- **Type Safety** with TypeScript throughout
- **State Management** using Redux Toolkit
- **Modern React** with hooks and functional components

## 🚀 Features

### Current Features
- **Authentication**: Secure login/logout with JWT tokens
- **Dashboard**: Overview of policies, claims, and account status
- **Policy Management**: View active policies and coverage details
- **Claims Management**: Track existing claims and submit new ones
- **Profile Management**: Update personal information and preferences
- **Responsive Design**: Works on desktop, tablet, and mobile devices

### Planned Features
- Real-time notifications
- Document upload and management
- Payment processing
- Live chat support
- Mobile app companion

## 🛠️ Technology Stack

- **Frontend Framework**: React 18 with TypeScript
- **UI Components**: Material-UI (MUI) v5
- **State Management**: Redux Toolkit
- **Routing**: React Router v6
- **Styling**: Emotion (CSS-in-JS)
- **Build Tool**: Create React App
- **Testing**: Jest + React Testing Library

## 🏃‍♂️ Getting Started

### Prerequisites
- Node.js 16+ and npm
- Backend services running (see main project README)

### Installation

1. **Install dependencies**:
   ```bash
   npm install
   ```

2. **Start development server**:
   ```bash
   npm start
   ```

3. **Open browser**:
   Navigate to [http://localhost:3000](http://localhost:3000)

### Demo Credentials
- **Email**: demo@lakesidemutual.com
- **Password**: demo123

## 📁 Project Structure

```
src/
├── components/          # Reusable UI components
│   ├── common/         # Generic components (buttons, forms, etc.)
│   └── layout/         # Layout components (header, footer, etc.)
├── hooks/              # Custom React hooks
├── pages/              # Page components for routing
├── services/           # API services and external integrations
├── store/              # Redux store configuration
│   └── slices/         # Redux slices for state management
├── theme/              # Material-UI theme configuration
├── types/              # TypeScript type definitions
└── utils/              # Utility functions and helpers
```

## 🎨 Design System

The portal implements Lakeside Mutual's design system with:

- **Primary Color**: `#1976d2` (Lakeside Blue)
- **Secondary Color**: `#00acc1` (Teal)
- **Typography**: Roboto font family
- **Spacing**: 8px base unit
- **Border Radius**: 8px default

## 🔧 Development

### Available Scripts

- `npm start` - Start development server
- `npm build` - Build for production
- `npm test` - Run test suite
- `npm run lint` - Run ESLint
- `npm run type-check` - Run TypeScript compiler

### Code Quality

The project enforces code quality through:

- **TypeScript** for type safety
- **ESLint** for code linting
- **Prettier** for code formatting
- **Husky** for git hooks (planned)

## 🧪 Testing Strategy

### Unit Tests
- Component testing with React Testing Library
- Hook testing with @testing-library/react-hooks
- Redux slice testing

### Integration Tests
- User workflow testing
- API integration testing
- Error boundary testing

### E2E Tests (Planned)
- Cypress for end-to-end testing
- Critical user journey coverage

## 🌐 API Integration

The portal integrates with backend microservices:

- **Customer Service**: Customer profile management
- **Policy Service**: Policy information and quotes
- **Claims Service**: Claims submission and tracking
- **Billing Service**: Payment processing (planned)

### API Configuration

```typescript
// Environment variables
REACT_APP_API_BASE_URL=http://localhost:8080
REACT_APP_AUTH_ENDPOINT=/auth
REACT_APP_ENABLE_MOCK_DATA=true
```

## 📱 Responsive Design

The portal is designed mobile-first with breakpoints:

- **Mobile**: 0px - 599px
- **Tablet**: 600px - 959px
- **Desktop**: 960px+

## 🔒 Security

Security measures implemented:

- **JWT Authentication** with secure token storage
- **Input Validation** on all forms
- **HTTPS Enforcement** in production
- **Content Security Policy** headers
- **XSS Protection** through React's built-in escaping

## 🚀 Deployment

### Production Build

```bash
# Create optimized production build
npm run build

# Serve static files
npm install -g serve
serve -s build
```

### Environment Configuration

Create `.env` files for different environments:

```env
# .env.production
REACT_APP_API_BASE_URL=https://api.lakesidemutual.com
REACT_APP_ENABLE_MOCK_DATA=false
```

## 🤝 Contributing

This project follows the main repository's contribution guidelines:

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

### Code Style

- Use TypeScript for all new code
- Follow existing naming conventions
- Add JSDoc comments for public APIs
- Keep components small and focused

## 📈 Performance

Performance optimizations implemented:

- **Code Splitting** with React.lazy()
- **Memoization** for expensive calculations
- **Virtual Scrolling** for large lists (planned)
- **Image Optimization** for faster loading
- **Bundle Analysis** with webpack-bundle-analyzer

## 🐛 Known Issues

- TypeScript compilation errors due to missing dependencies (resolved after `npm install`)
- Some Material-UI theme customizations may not apply in development mode
- Error boundary fallback needs styling improvements

## 📄 License

This project is part of the Lakeside Mutual demonstration system and is provided for educational purposes.

## 📞 Support

For technical support or questions:

- **Project Issues**: GitHub Issues
- **Documentation**: See `/docs` in main repository
- **Architecture Questions**: See ADRs in `/docs/architecture`

---

Built with ❤️ by the Lakeside Mutual Development Team
