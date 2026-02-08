from sqlalchemy import Column, Integer, String
from db.session import Base

class Integration(Base):
    __tablename__ = "integration"
    
    id = Column(Integer, primary_key=True)
    service_name = Column(String, nullable=False, unique=True)
    api_token = Column(String, nullable=False)